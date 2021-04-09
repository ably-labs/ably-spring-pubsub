package com.example.ably.hibernate;

import com.example.ably.hibernate.entities.TodoList;
import com.example.ably.hibernate.entities.TodoItem;
import com.example.ably.hibernate.jao.TodoItemRepository;
import com.example.ably.hibernate.jao.TodoListRepository;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HibernateService {
	private final TodoItemRepository todoItemRepository;
	private final TodoListRepository todoListRepository;

	@Autowired
	public HibernateService(
		TodoItemRepository todoItemRepository,
		TodoListRepository todoListRepository) {

		this.todoItemRepository = todoItemRepository;
		this.todoListRepository = todoListRepository;
	}

	@Transactional
	public TodoItem addData(String channelName, String data, String clientID) {
		TodoList todoList = todoListRepository.findByName(channelName);
		if (todoList == null) {
			todoList = todoListRepository.save(new TodoList(channelName));
		}
		return todoItemRepository.save(new TodoItem(data, clientID, todoList));
	}

	@Transactional
	public void removeData(long id) {
		Optional<TodoItem> todoItem = todoItemRepository.findById(id);
		if (!todoItem.isPresent()) {
			return;
		}
		todoItemRepository.delete(todoItem.get());
	}

	@Transactional
	public void markComplete(long id) {
		todoItemRepository.getOne(id).setCompleted(true);
	}

	@Transactional
	public void markIncomplete(long id) {
		todoItemRepository.getOne(id).setCompleted(false);
	}
}
