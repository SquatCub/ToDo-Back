
package com.bran.todoback.repository;

import com.bran.todoback.entity.Todo;
import com.bran.todoback.utils.Priority;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class TodoRepository implements TodoDAO {

    List<Todo> todoList = new ArrayList<>();

    @Override
    public List<Todo> findAll() {
        return todoList;
    }

    @Override
    public List<Todo> findByName(String name) {
        List<Todo> filteredList = todoList.stream().filter(a ->
                a.getName().toLowerCase().contains(name.toLowerCase())).toList();
        return filteredList;
    }
    @Override
    public List<Todo> findByPriority(String priority) {
        List<Todo> filteredList = todoList.stream().filter(a ->
                a.getPriority() == Priority.valueOf(priority.toUpperCase())).toList();
        return filteredList;
    }
    @Override
    public List<Todo> findByDone(Boolean done) {
        List<Todo> filteredList = todoList.stream().filter(a -> a.getDone().equals(done)).toList();
        return filteredList;
    }
    @Override
    public List<Todo> findByNameAndPriority(String name, String priority) {
        List<Todo> filteredListByName = todoList.stream().filter(a ->
                a.getName().toLowerCase().contains(name.toLowerCase())).toList();
        List<Todo> filteredListByPriority = filteredListByName.stream().filter(a ->
                a.getPriority() == Priority.valueOf(priority.toUpperCase())).toList();
        List<Todo> filteredList = filteredListByPriority;
        return  filteredList;
    }
    @Override
    public List<Todo> findByNameAndDone(String name, Boolean done) {
        List<Todo> filteredListByName = todoList.stream().filter(a ->
                a.getName().toLowerCase().contains(name.toLowerCase())).toList();
        List<Todo> filteredListByDone = filteredListByName.stream().filter(a ->
                a.getDone().equals(done)).toList();
        List<Todo> filteredList = filteredListByDone;
        return  filteredList;
    }
    @Override
    public List<Todo> findByPriorityAndDone(String priority, Boolean done) {
        List<Todo> filteredListByPriority = todoList.stream().filter(a ->
                a.getPriority() == Priority.valueOf(priority.toUpperCase())).toList();
        List<Todo> filteredListByDone = filteredListByPriority.stream().filter(a ->
                a.getDone().equals(done)).toList();
        List<Todo> filteredList = filteredListByDone;
        return  filteredList;
    }
    @Override
    public List<Todo> findByNameAndPriorityAndDone(String name, String priority, Boolean done) {
        List<Todo> filteredListByName = todoList.stream().filter(a ->
                a.getName().toLowerCase().contains(name.toLowerCase())).toList();
        List<Todo> filteredListByPriority = filteredListByName.stream().filter(a ->
                a.getPriority() == Priority.valueOf(priority.toUpperCase())).toList();
        List<Todo> filteredListByDone = filteredListByPriority.stream().filter(a ->
                a.getDone() == done).toList();
        List<Todo> filteredList = filteredListByDone;
        return  filteredList;
    }

    @Override
    public List<Todo> sortByDueDate() {
        List<Todo> filteredList = todoList.stream()
                .sorted(Comparator.nullsLast(Comparator.comparing(Todo::getDue_date,
                                Comparator.nullsLast(Comparator.naturalOrder()))))
                .collect(Collectors.toList());
        return filteredList;
    }

    @Override
    public List<Todo> sortByPriority() {
        List<Todo> filteredList = todoList.stream()
                .sorted(Comparator.nullsLast(Comparator.comparing(Todo::getPriorityByValue,
                        Comparator.nullsLast(Comparator.naturalOrder()))))
                .collect(Collectors.toList());
        return filteredList;
    }



    @Override
    public Todo save(Todo todo) {
        String uniqueID = UUID.randomUUID().toString();
        todo.setId(uniqueID);
        todo.setDone(false);
        if(todo.getPriority() == null)
            todo.setPriority(Priority.LOW);
        todo.setCreation_date(LocalDateTime.now());
        todoList.add(todo);
        return todo;
    }

    @Override
    public Todo save(String id, Todo todo) {
        Todo updatedTodo = findById(id);
        int newTodoIndex = getTodoIndex(updatedTodo);
        updatedTodo.setId(id);
        updatedTodo.setName(todo.getName());
        updatedTodo.setPriority(todo.getPriority());
        updatedTodo.setDue_date(todo.getDue_date());
        todoList.set(newTodoIndex, updatedTodo);
        return updatedTodo;
    }

    @Override
    public Todo save(String id, Boolean done) {
        Todo updatedTodo = findById(id);
        int newTodoIndex = getTodoIndex(updatedTodo);
        updatedTodo.setId(id);
        if(!((done == false && updatedTodo.getDone() == false) ||
                (done == true && updatedTodo.getDone() == true))) {
            if(done == false) {
                updatedTodo.setDone(false);
                updatedTodo.setDone_date(null);
            } else {
                updatedTodo.setDone(true);
                updatedTodo.setDone_date(LocalDateTime.now());
            }
        }
        todoList.set(newTodoIndex, updatedTodo);
        return updatedTodo;
    }

    @Override
    public void delete(String id) {
        Todo deletedTodo = findById(id);
        int newTodoIndex = getTodoIndex(deletedTodo);
        todoList.remove(newTodoIndex);
    }

    @Override
    public Todo findById(String id) {
        Todo newTodo;
        try {newTodo = todoList.stream().filter(a ->
                    a.getId().equals(id)).toList().get(0);
        } catch(ArrayIndexOutOfBoundsException e) {
            newTodo = null;
        }
        return newTodo;
    }
    public int getTodoIndex(Todo todo) {
        return todoList.indexOf(todo);
    }
}
