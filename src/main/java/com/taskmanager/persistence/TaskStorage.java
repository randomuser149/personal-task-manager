package com.taskmanager.persistence;

import com.taskmanager.exception.InvalidTaskDataException;
import com.taskmanager.model.PersonalTask;
import com.taskmanager.model.Task;
import com.taskmanager.model.WorkTask;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TaskStorage {
    private final String filePath;

    public TaskStorage(String filePath) {
        this.filePath = filePath;
    }

    public List<Task> loadTasks() throws IOException, InvalidTaskDataException {
        List<Task> tasks = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return tasks;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length < 6) continue;

                String type = parts[0];
                String id = parts[1];
                String title = parts[2];
                int priority = Integer.parseInt(parts[3]);
                boolean completed = Boolean.parseBoolean(parts[4]);

                Task task;
                if ("WORK".equalsIgnoreCase(type)) {
                    task = new WorkTask(id, title, priority, parts[5]);
                } else {
                    boolean urgent = Boolean.parseBoolean(parts[5]);
                    task = new PersonalTask(id, title, priority, urgent);
                }
                task.setCompleted(completed);
                tasks.add(task);
            }
        }
        return tasks;
    }

    public void saveTasks(List<Task> tasks) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Task task : tasks) {
                if (task instanceof WorkTask workTask) {
                    writer.write(String.format("WORK,%s,%s,%d,%b,%s%n",
                            workTask.getId(), workTask.getTitle(), workTask.getPriority(),
                            workTask.isCompleted(), workTask.getProjectCode()));
                } else if (task instanceof PersonalTask personalTask) {
                    writer.write(String.format("PERSONAL,%s,%s,%d,%b,%b%n",
                            personalTask.getId(), personalTask.getTitle(), personalTask.getPriority(),
                            personalTask.isCompleted(), personalTask.isUrgent()));
                }
            }
        }
    }

    public String getNextAvailableId(List<Task> tasks) {
        int maxId = 0;
        for (Task task : tasks) {
            try {
                int idNum = Integer.parseInt(task.getId());
                if (idNum > maxId) {
                    maxId = idNum;
                }
            } catch (NumberFormatException ignored) {
            }
        }
        return String.valueOf(maxId + 1);
    }
}
