package com.bran.todoback.repository;

import com.bran.todoback.entity.Todo;

import java.time.LocalDate;
import java.util.List;

public interface TodoDAO {

    List<Todo> findAll();
    List<Todo> findByName(String name);
    List<Todo> findByPriority(String priority);
    List<Todo> findByDone(Boolean done);
    List<Todo> findByNameAndPriority(String name, String priority);
    List<Todo> findByNameAndDone(String name, Boolean done);
    List<Todo> findByPriorityAndDone(String priority, Boolean done);
    List<Todo> findByNameAndPriorityAndDone(String name, String priority, Boolean done);

    List<Todo> sortByDueDate();
    List<Todo> sortByPriority();
    Todo save(Todo todo);
    Todo save(String id, Todo todo);
    Todo save(String id, Boolean done);
    void delete(String id);
    Todo findById(String id);

}
