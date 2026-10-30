package com.taskmanager.strategy;

import com.taskmanager.model.Task;

public interface TaskFilterStrategy {
    boolean matches(Task task);
}
