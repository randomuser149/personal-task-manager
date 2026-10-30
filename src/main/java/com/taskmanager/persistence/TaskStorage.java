package com.taskmanager.persistence;

import com.taskmanager.exception.InvalidTaskDataException;
import com.taskmanager.model.PersonalTask;
import com.taskmanager.model.Task;
import com.taskmanager.model.WorkTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TaskStorage {
    private String filePath;

    public TaskStorage(String filePath) {
        this.filePath = filePath;
    }

    public void saveTasks(List<Task> tasks) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Task task : tasks) {
                if (task instanceof WorkTask) {
                    WorkTask workTask = (WorkTask) task;
                    writer.write("WORK," + workTask.getId() + "," + workTask.getTitle() + "," 
                            + workTask.getPriority() + "," + workTask.isCompleted() + "," + workTask.getProjectCode());
                } else if (task instanceof PersonalTask) {
                    PersonalTask personalTask = (PersonalTask) task;
                    writer.write("PERSONAL," + personalTask.getId() + "," + personalTask.getTitle() + "," 
                            + personalTask.getPriority() + "," + personalTask.isCompleted() + "," + personalTask.isUrgent());
                }
                writer.newLine();
            }
        }
    }

    public List<Task> loadTasks() throws IOException, InvalidTaskDataException {
        List<Task> tasks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                String type = parts[0];
                String id = parts[1];
                String title = parts[2];
                int priority = Integer.parseInt(parts[3]);
                boolean completed = Boolean.parseBoolean(parts[4]);

                Task task = null;
                if ("WORK".equals(type)) {
                    String projectCode = parts[5];
                    task = new WorkTask(id, title, priority, projectCode);
                } else if ("PERSONAL".equals(type)) {
                    boolean urgent = Boolean.parseBoolean(parts[5]);
                    task = new PersonalTask(id, title, priority, urgent);
                }

                if (task != null) {
                    task.setCompleted(completed);
                    tasks.add(task);
                }
            }
        }
        return tasks;
    }
}
