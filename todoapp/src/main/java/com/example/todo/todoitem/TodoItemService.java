package com.example.todo.todoitem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoItemService {
    @Autowired
    private TodoItemRepository todoItemRepository;

    public List<TodoItem> findAll() {
        return todoItemRepository.findAll();
    }

    public Optional<TodoItem> find(Long id) {
        return todoItemRepository.findById(id);
    }

    public TodoItem create(String text, String username) {
        TodoItem copy = new TodoItem(text, username);
        return todoItemRepository.save(copy);
    }

    public Optional<TodoItem> updateCompletionStatus(Long id, boolean status) {
        return todoItemRepository.findById(id)
                .map(oldItem -> {
                    oldItem.setCompleted(status);
                    return todoItemRepository.save(oldItem);
                });
    }

    public void delete(Long id) {
        todoItemRepository.deleteById(id);
    }
}
