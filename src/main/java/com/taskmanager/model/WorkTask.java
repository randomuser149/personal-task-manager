package com.taskmanager.model;

import com.taskmanager.exception.InvalidTaskDataException;

public class WorkTask extends Task {
    private String projectCode;

    public WorkTask(String id, String title, int priority, String projectCode) throws InvalidTaskDataException {
        super(id, title, priority);
        if (projectCode == null || projectCode.trim().isEmpty()) {
            throw new InvalidTaskDataException("Project code cannot be empty.");
        }
        this.projectCode = projectCode;
    }

    public String getProjectCode() {
        return projectCode;
    }

    @Override
    public int calculatePriorityScore() {
        return getPriority() * 2;
    }

    @Override
    public String toString() {
        return super.toString() + " [Work: " + projectCode + "]";
    }
}
