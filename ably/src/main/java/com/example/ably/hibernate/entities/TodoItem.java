package com.example.ably.hibernate.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import javax.persistence.Entity;
import lombok.*;
import org.hibernate.annotations.*;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class TodoItem
{
	@Id
	@GeneratedValue
	private Long id;

	@NonNull private String text;

	@NonNull private String clientID;

	@NonNull private boolean completed;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "todo_list_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private TodoList todoList;

	public TodoItem(String text, String clientID, TodoList todoList)
	{
		this.text = text;
		this.clientID = clientID;
		this.todoList = todoList;
		this.completed = false;
	}
}