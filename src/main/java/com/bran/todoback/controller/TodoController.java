package com.bran.todoback.controller;

import com.bran.todoback.entity.Todo;
import com.bran.todoback.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@RestController
public class TodoController {

    @Autowired
    TodoService todoService;

    @GetMapping("/todos")
    public HashMap<String, Object> getTodos(@RequestParam(required = false, defaultValue = "1") Integer page,
                               @RequestParam(required = false, defaultValue = "") String name,
                               @RequestParam(required = false, defaultValue = "") String priority,
                               @RequestParam(required = false, defaultValue = "") Boolean done,
                               @RequestParam(required = false, defaultValue = "") Boolean orderByPriority,
                               @RequestParam(required = false, defaultValue = "") Boolean orderByDueDate,
                               @RequestParam(required = false, defaultValue = "") Boolean orderByPriorityAndDueDate,
                               @RequestParam(required = false, defaultValue = "") Boolean orderByDueDateAndPriority) {
        return todoService.getAllTodos(page, name, priority, done, orderByPriority,
                orderByDueDate, orderByPriorityAndDueDate, orderByDueDateAndPriority);
    }

    @GetMapping("/todos-date")
    public List<Todo> getTodosByDueDate() {
        return todoService.getTodosByDate();
    }

    @GetMapping("/todos-priority")
    public List<Todo> getTodosByPriority() {
        return todoService.getTodosByPriority();
    }

    @PostMapping("/todos")
    public HashMap<String, Object> createTodo(@RequestBody Todo todo) {
        return todoService.createTodo(todo);
    }

    @PutMapping("/todos/{id}")
    public HashMap<String, Object> updateTodo(@PathVariable String id, @RequestBody Todo todo) {
        return todoService.updateTodo(id, todo);
    }
    @DeleteMapping("/todos/{id}")
    public HashMap<String, Object> deleteTodo(@PathVariable String id) {
        return todoService.deleteTodo(id);
    }

    @PutMapping("/todos/{id}/done")
    public HashMap<String, Object> updateTodoDone(@PathVariable String id) {
        return todoService.updateTodo(id, true);
    }

    @PutMapping("/todos/{id}/undone")
    public HashMap<String, Object> updateTodoUndone(@PathVariable String id) {
        return todoService.updateTodo(id, false);
    }

    @GetMapping("/todos/metrics")
    public HashMap<String, Object> getTodosMetrics() {
        return todoService.getTodosMetrics();
    }
}
