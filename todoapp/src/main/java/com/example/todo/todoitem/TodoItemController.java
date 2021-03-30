package com.example.todo.todoitem;

import io.ably.lib.rest.AblyRest;
import io.ably.lib.types.AblyException;
import io.ably.lib.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api")
public class TodoItemController {

    @Autowired
    private TodoItemService todoItemService;

    private AblyRest ablyRest;

    @Value( "${ABLY_API_KEY}" )
    private void setAblyRest(String apiKey) throws AblyException {
        ablyRest = new AblyRest(apiKey);
    }

    private final String CHANNEL_NAME = "default";

    @GetMapping
    public ResponseEntity<List<TodoItem>> findAll() {
        List<TodoItem> items = todoItemService.findAll();
        return ResponseEntity.ok().body(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoItem> find(@PathVariable("id") Long id) {
        Optional<TodoItem> item = todoItemService.find(id);
        return ResponseEntity.of(item);
    }

    @PostMapping
    public ResponseEntity<TodoItem> create(@CookieValue(value = "username") String username, @RequestBody Map<String, String> json) {
        if (json.get("text") == null)
        {
            return ResponseEntity.badRequest().body(null);
        }
        TodoItem newItem = todoItemService.create(json.get("text"), username);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newItem.getId())
                .toUri();

        JsonUtils.JsonUtilsObject object = io.ably.lib.util.JsonUtils.object();
        object.add("text", json.get("text"));
        object.add("username", username);
        object.add("id", newItem.getId().toString());

        publishToChannel("add", object.toJson());
        return ResponseEntity.created(location).body(newItem);
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<TodoItem> completed(
            @PathVariable("id") Long id) {

        Optional<TodoItem> updated = todoItemService.updateCompletionStatus(id, true);

        return updated
                .map(value -> {
                    publishToChannel("complete", Long.toString(id));
                    return ResponseEntity.ok().body(value);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/uncomplete")
    public ResponseEntity<TodoItem> uncompleted(
            @PathVariable("id") Long id) {

        Optional<TodoItem> updated = todoItemService.updateCompletionStatus(id, false);
        return updated
                .map(value -> {
                    publishToChannel("incomplete", Long.toString(id));
                    return ResponseEntity.ok().body(value);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TodoItem> delete(
            @CookieValue(value = "username") String username,
            @PathVariable("id") Long id) {

        Optional<TodoItem> todoOptional = todoItemService.find(id);
        if (todoOptional.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        if (todoOptional.get().getUsername().equals(username)) {
            todoItemService.delete(id);
            publishToChannel("remove", Long.toString(id));
        }
        return ResponseEntity.noContent().build();
    }

    /* Publish an update to the TODO list to an Ably Channel */
    private boolean publishToChannel(String name, Object data) {
        try {
            ablyRest.channels.get(CHANNEL_NAME).publish(name, data);
        } catch (AblyException err) {
            System.out.println(err.errorInfo);
            return false;
        }
        return true;
    }
}
