package com.bran.todoback.entity;

import com.bran.todoback.utils.Priority;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Todo {

    @Id
    private String id;
    private String name;
    private LocalDate due_date;
    private Boolean done;
    private LocalDateTime done_date;
    private Priority priority;
    private LocalDateTime creation_date;

    public Todo() { }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDue_date() {
        return due_date;
    }

    public void setDue_date(LocalDate due_date) {
        this.due_date = due_date;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public LocalDateTime getDone_date() {
        return done_date;
    }

    public void setDone_date(LocalDateTime done_date) {
        this.done_date = done_date;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public LocalDateTime getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(LocalDateTime creation_date) {
        this.creation_date = creation_date;
    }

    public int getPriorityByValue() {
        return Integer.parseInt(priority.getPriority());
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", due_date=" + due_date +
                ", done=" + done +
                ", done_date=" + done_date +
                ", priority=" + priority +
                ", creation_date=" + creation_date +
                '}';
    }
}
