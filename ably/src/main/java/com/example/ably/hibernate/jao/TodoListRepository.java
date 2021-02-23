package com.example.ably.hibernate.jao;

import com.example.ably.hibernate.entities.TodoList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoListRepository extends JpaRepository<TodoList, Long> {
	TodoList findByName(String name);
}
