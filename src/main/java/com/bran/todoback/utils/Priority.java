package com.bran.todoback.utils;

public enum Priority {
    LOW("0"), MEDIUM("1"), HIGH("3");
    private String priority;

    public String getPriority() {
        return this.priority;
    }
    private Priority(String priority) {
        this.priority = priority;
    }
}
