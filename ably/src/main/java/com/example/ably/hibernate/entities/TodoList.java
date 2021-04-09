package com.example.ably.hibernate.entities;

import javax.persistence.*;
import lombok.*;

@Entity
@Data
@Table
@RequiredArgsConstructor
public class TodoList
{
	@Id
	@GeneratedValue
	private Long id;

	private String name;

	public TodoList(String name) {
		this.name = name;
	}
}
