package com.taskmanager.strategy;

import com.taskmanager.model.Task;

public class PriorityFilter implements TaskFilterStrategy {
    private int minPriority;

    public PriorityFilter(int minPriority) {
        this.minPriority = minPriority;
    }

    @Override
    public boolean matches(Task task) {
        return task.getPriority() >= minPriority;
    }
}
