package com.taskmanager.strategy;

import com.taskmanager.model.Task;

public class CompletionFilter implements TaskFilterStrategy {
    private boolean showCompleted;

    public CompletionFilter(boolean showCompleted) {
        this.showCompleted = showCompleted;
    }

    @Override
    public boolean matches(Task task) {
        return task.isCompleted() == showCompleted;
    }
}
