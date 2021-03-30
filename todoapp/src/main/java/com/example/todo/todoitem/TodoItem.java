package com.example.todo.todoitem;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class TodoItem
{
    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private String text;

    @NonNull
    private String username;

    private boolean completed = false;
}