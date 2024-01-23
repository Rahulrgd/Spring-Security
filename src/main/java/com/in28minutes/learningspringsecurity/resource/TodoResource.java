package com.in28minutes.learningspringsecurity.resource;

import jakarta.annotation.security.RolesAllowed;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TodoResource {

  private Logger logger = LoggerFactory.getLogger(getClass());

  public static List<Todo> TODOS_LIST = List.of(
    new Todo("in28minutes", "Learn AWS"),
    new Todo("in28minutes", "Learn Google Cloude"),
    new Todo("in28minutes", "Learn Azure")
  );

  @GetMapping("/todos")
  public List<Todo> retriveAllTodos() {
    return TODOS_LIST;
  }

  @GetMapping(path = "/users/{username}/todos")
  public Todo retrieveTodosForSpecificUser(@PathVariable String username) {
    return TODOS_LIST.get(0);
  }
  @PostMapping(path = "/users/{username}/todos")
  public void createTodosForSpecificUser(@PathVariable String username, @RequestBody Todo todo) {
    logger.info("Create {} for {}", username, todo);
  }
}

record Todo(String username, String description) {}
