package com.example.todo;

import com.example.todo.todoitem.TodoItem;
import com.example.todo.todoitem.TodoItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TodoListController {
    @Autowired
    private TodoItemService todoItemService;

    @GetMapping("/")
    public String todoPage(Model model) {
        List<TodoItem> listItems = todoItemService.findAll();
        if (listItems == null) {
            listItems = new ArrayList<>();
        }
        model.addAttribute("todoitems", listItems);

        return "todolist/index";
    }
}
