package com.taskmanager.model;

import com.taskmanager.exception.InvalidTaskDataException;

public class PersonalTask extends Task {
    private boolean urgent;

    public PersonalTask(String id, String title, int priority, boolean urgent) throws InvalidTaskDataException {
        super(id, title, priority);
        this.urgent = urgent;
    }

    public boolean isUrgent() {
        return urgent;
    }

    @Override
    public int calculatePriorityScore() {
        return urgent ? getPriority() + 3 : getPriority();
    }

    @Override
    public String toString() {
        String urgentTag = urgent ? " (URGENT)" : "";
        return super.toString() + " [Personal" + urgentTag + "]";
    }
}
