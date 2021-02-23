package com.example.ably.hibernate.jao;

import com.example.ably.hibernate.entities.TodoItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {

	List<TodoItem> findByTodoListName(String name);
}
