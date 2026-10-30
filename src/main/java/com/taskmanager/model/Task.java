package com.taskmanager.model;

import com.taskmanager.exception.InvalidTaskDataException;

public abstract class Task implements Comparable<Task> {
    private String id;
    private String title;
    private int priority;
    private boolean completed;

    public Task(String id, String title, int priority) throws InvalidTaskDataException {
        if (title == null || title.trim().isEmpty()) {
            throw new InvalidTaskDataException("Task title cannot be empty.");
        }
        if (priority < 1 || priority > 5) {
            throw new InvalidTaskDataException("Priority must be between 1 and 5.");
        }
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.completed = false;
    }

    public abstract int calculatePriorityScore();

    public String getId() { return id; }
    public String getTitle() { return title; }
    public int getPriority() { return priority; }
    public boolean isCompleted() { return completed; }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public int compareTo(Task other) {
        return Integer.compare(other.calculatePriorityScore(), this.calculatePriorityScore());
    }

    @Override
    public String toString() {
        String status = completed ? "[X]" : "[ ]";
        return status + " ID: " + id + " | " + title + " (Priority: " + priority + ", Score: " + calculatePriorityScore() + ")";
    }
}
