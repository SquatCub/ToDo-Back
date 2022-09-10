package com.bran.todoback.service;

import com.bran.todoback.entity.Todo;
import com.bran.todoback.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class TodoService {

    @Autowired
    TodoRepository todoRepository;
    int itemsPerPage = 10;
    public HashMap<String, Object> getAllTodos(Integer page, String name, String priority, Boolean done,
                                                Boolean orderByPriority, Boolean orderByDueDate,
                                                Boolean orderByPriorityAndDueDate,
                                                Boolean orderByDueDateAndPriority) {

        HashMap<String, Object> response = new HashMap<>();
        List<Todo> todoList;
        if(!name.equals("") && !priority.equals("") && done != null) {
            todoList = todoRepository.findByNameAndPriorityAndDone(name, priority, done);
        } else if (!name.equals("") && !priority.equals("")) {
            todoList = todoRepository.findByNameAndPriority(name, priority);
        } else if (!name.equals("") && done != null) {
            todoList = todoRepository.findByNameAndDone(name, done);
        } else if(!priority.equals("") && done != null) {
            todoList = todoRepository.findByPriorityAndDone(priority, done);
        } else if(!name.equals("")) {
            todoList = todoRepository.findByName(name);
        } else if(!priority.equals("")) {
            todoList = todoRepository.findByPriority(priority);
        } else if(done != null) {
            todoList = todoRepository.findByDone(done);
        } else {
            todoList = todoRepository.findAll();
        }

        if(orderByPriorityAndDueDate != null || orderByDueDateAndPriority != null) {
            todoList = sortByDueDate(todoList);
            if(orderByDueDate == false) {
                Collections.reverse(todoList);
            }
            todoList = sortByPriority(todoList);
            if(orderByPriority == false) {
                Collections.reverse(todoList);
            }
        } else if(orderByPriority != null) {
            todoList = sortByPriority(todoList);
            if(orderByPriority == false) {
                Collections.reverse(todoList);
            }
        } else if(orderByDueDate != null) {
            todoList = sortByDueDate(todoList);
            if(orderByDueDate == false) {
                Collections.reverse(todoList);
            }
        }

        int fromIndex = (page - 1) * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, todoList.size());
        List<Todo> filteredList = todoList.subList(fromIndex, toIndex);

        int size = todoList.size();

        response.put("code", 200);
        response.put("data", filteredList);
        response.put("size", size);
        return response;
    }
    public List<Todo> getTodosByDate() {
        return todoRepository.sortByDueDate();
    }

    public List<Todo> getTodosByPriority() {
        return todoRepository.sortByPriority();
    }

    public HashMap<String, Object> createTodo(Todo todo) {
        System.out.println(todo);
        HashMap<String, Object> response = new HashMap<>();
        if(todo.getName().length() > 120) {
            response.put("code", 400);
            response.put("message", "name length is greater than 120 chars");
        } else {
            todoRepository.save(todo);
            response.put("code", 200);
            response.put("message", "todo created");
        }
        return response;
    }

    public HashMap<String, Object> updateTodo(String id, Todo todo) {
        HashMap<String, Object> response = new HashMap<>();
        if(todo.getName().length() > 120) {
            response.put("code", 400);
            response.put("message", "name length is greater than 120 chars");
        } else {
            todoRepository.save(id, todo);
            response.put("code", 200);
            response.put("message", "todo updated");
        }
        return response;
    }

    public HashMap<String, Object> updateTodo(String id, Boolean done) {
        HashMap<String, Object> response = new HashMap<>();
        if(todoRepository.findById(id) == null) {
            response.put("code", 400);
            response.put("message", "todo not found");
        } else {
            todoRepository.save(id, done);
            response.put("code", 200);
            response.put("message", "todo updated");
        }
        return response;
    }

    public HashMap<String, Object> deleteTodo(String id) {
        HashMap<String, Object> response = new HashMap<>();
        if(todoRepository.findById(id) != null) {
            todoRepository.delete(id);
            response.put("code", 200);
            response.put("message", "todo deleted");
        } else {
            response.put("code", 400);
            response.put("message", "todo not found");
        }
        return response;
    }

    public HashMap<String, Object> getTodosMetrics() {
        HashMap<String, Object> response = new HashMap<>();
        List<Todo> todoList = todoRepository.findAll();

        long totalSeconds = 0l;
        long lowPrioritySeconds = 0l;
        long mediumPrioritySeconds = 0l;
        long highPrioritySeconds = 0l;
        for(Todo todo: todoList) {
            if(todo.getDone_date() != null) {
                Duration duration = Duration.between(todo.getCreation_date(), todo.getDone_date());
                totalSeconds+=duration.getSeconds();
                switch (todo.getPriority().getPriority()) {
                    case "0":
                        lowPrioritySeconds+=duration.getSeconds();
                        break;
                    case "1":
                        mediumPrioritySeconds+=duration.getSeconds();
                        break;
                    case "2":
                        highPrioritySeconds+=duration.getSeconds();
                        break;
                    default:
                        break;
                }
            }
        }
        HashMap<String, Object> metricsResponse = new HashMap<>();
        metricsResponse.put("total", formatDate(totalSeconds));
        metricsResponse.put("low", formatDate(lowPrioritySeconds));
        metricsResponse.put("medium", formatDate(mediumPrioritySeconds));
        metricsResponse.put("high", formatDate(highPrioritySeconds));

        response.put("code", 200);
        response.put("data", metricsResponse);

        return response;
    }

    String formatDate(long seconds) {
        if(seconds==0)
            return "-";
        String formattedDate = "";
        int days = (int)TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (days *24);
        long minutes = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60);
        if(days > 0)
            formattedDate += days + ":";
        formattedDate += hours + ":" + minutes + " minutes";
        return formattedDate;
    }

    public List<Todo> sortByDueDate(List<Todo> todoList) {
        List<Todo> filteredList = todoList.stream()
                .sorted(Comparator.nullsLast(Comparator.comparing(Todo::getDue_date,
                        Comparator.nullsLast(Comparator.naturalOrder()))))
                .collect(Collectors.toList());
        return filteredList;
    }

    public List<Todo> sortByPriority(List<Todo> todoList) {
        List<Todo> filteredList = todoList.stream()
                .sorted(Comparator.nullsLast(Comparator.comparing(Todo::getPriorityByValue,
                        Comparator.nullsLast(Comparator.naturalOrder()))))
                .collect(Collectors.toList());
        return filteredList;
    }
}
