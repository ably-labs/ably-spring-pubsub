package com.example.ably.controllers;

import com.example.ably.AblyConfig;
import com.example.ably.hibernate.HibernateService;
import com.example.ably.hibernate.entities.TodoItem;
import com.example.ably.hibernate.entities.TodoList;
import com.example.ably.hibernate.jao.TodoItemRepository;
import com.example.ably.hibernate.jao.TodoListRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.ably.lib.rest.AblyRest;
import io.ably.lib.types.AblyException;
import io.ably.lib.types.Message;
import io.ably.lib.util.JsonUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class AblyController
{
	private AblyRest ablyRest;

	@Autowired
	private AblyConfig ablyConfig;

	@Autowired
	private TodoItemRepository todoItemRepository;

	@Autowired
	private TodoListRepository todoListRepository;

	@Autowired
	private HibernateService hibernateService;

	private AblyRest getRest() {
		if (ablyRest == null) {
			ablyRest = ablyConfig.ablyRest();
		}
		return ablyRest;
	}

	/* Publish an update to the TODO list to an Ably Channel */
	private boolean publishToChannel(String channelName, String name, Object data) {
		try {
			getRest().channels.get(channelName).publish(name, data);
		} catch (AblyException err) {
			System.out.println(err.getMessage());
			return false;
		}
		return true;
	}

	/* Get a TODO list page */
	@GetMapping("/")
	public String getIndex(Model model) {
		List<TodoList> listItems = todoListRepository.findAll();
		model.addAttribute("lists", listItems);

		return "index/index";
	}

	/* Create a new TODO item */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public boolean addTodo(@RequestBody Map<String, String> json)
	{
		try {
			TodoItem newItem = hibernateService.addData(json.get("channelName"),
				json.get("text"), json.get("clientID"));
			json.put("id", newItem.getId().toString());

			JsonUtils.JsonUtilsObject object = io.ably.lib.util.JsonUtils.object();
			object.add("text", json.get("text"));
			object.add("clientID", json.get("clientID"));
			object.add("id", newItem.getId().toString());

			return publishToChannel(json.get("channelName"), "add", object.toJson());
		} catch (EntityNotFoundException err) {
			return false;
		}
	}

	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	@ResponseBody
	public boolean removeTodo(@RequestBody long id) {
		TodoItem item;
		try {
			item = todoItemRepository.getOne(id);
			String channelName = item.getTodoList().getName();
			hibernateService.removeData(id);
			return publishToChannel(channelName, "remove", Long.toString(id));
		} catch (EntityNotFoundException err) {
			return false;
		}
	}

	@RequestMapping(value = "/complete", method = RequestMethod.POST)
	@ResponseBody
	public boolean completeTodo(@RequestBody long id) {
		try {
			TodoItem item = todoItemRepository.getOne(id);
			String channelName = item.getTodoList().getName();
			hibernateService.markComplete(id);
			return publishToChannel(channelName, "complete", Long.toString(id));
		} catch (EntityNotFoundException err) {
			return false;
		}
	}

	@RequestMapping(value = "/uncomplete", method = RequestMethod.POST)
	@ResponseBody
	public boolean uncompleteTodo(@RequestBody long id) {
		try {
			TodoItem item = todoItemRepository.getOne(id);
			String channelName = item.getTodoList().getName();
			hibernateService.markIncomplete(id);
			return publishToChannel(channelName, "incomplete", Long.toString(id));
		} catch (EntityNotFoundException err) {
			return false;
		}
	}

	/* Get a TODO list page */
	@GetMapping("/todo/{channelname}")
	public String getTODO(@PathVariable("channelname") String channelname, Model model) {
		model.addAttribute("channelname", channelname);
		List<TodoItem> listItems = todoItemRepository.findByTodoListName(channelname);
		if (listItems == null) {
			listItems = new ArrayList<>();
		}
		model.addAttribute("todoitems", listItems);

		return "todo/index";
	}
}
