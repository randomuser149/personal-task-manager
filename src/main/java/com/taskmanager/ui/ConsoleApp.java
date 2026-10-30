package com.taskmanager.ui;

import com.taskmanager.exception.InvalidTaskDataException;
import com.taskmanager.model.PersonalTask;
import com.taskmanager.model.Task;
import com.taskmanager.model.WorkTask;
import com.taskmanager.persistence.TaskStorage;
import com.taskmanager.strategy.PriorityFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class ConsoleApp {
    private static List<Task> tasks = new ArrayList<>();
    private static TaskStorage storage = new TaskStorage("tasks.csv");

    public static void main(String[] args) {
        loadData();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=== PERSONAL TASK MANAGER ===");

        while (running) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. View All Tasks");
            System.out.println("2. Add Work Task");
            System.out.println("3. Add Personal Task");
            System.out.println("4. Mark Task Completed");
            System.out.println("5. Filter Tasks by High Priority (Priority >= 3)");
            System.out.println("6. Save & Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        viewTasks(tasks);
                        break;
                    case "2":
                        addWorkTask(scanner);
                        break;
                    case "3":
                        addPersonalTask(scanner);
                        break;
                    case "4":
                        markTaskCompleted(scanner);
                        break;
                    case "5":
                        filterHighPriority();
                        break;
                    case "6":
                        storage.saveTasks(tasks);
                        System.out.println("Tasks saved successfully. Goodbye!");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (InvalidTaskDataException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("File Storage Error: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void loadData() {
        try {
            tasks = storage.loadTasks();
            System.out.println("Loaded " + tasks.size() + " tasks from storage.");
        } catch (IOException | InvalidTaskDataException e) {
            System.out.println("No previous data loaded. Starting fresh.");
        }
    }

    private static void viewTasks(List<Task> taskList) {
        if (taskList.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }
        Collections.sort(taskList);
        for (Task task : taskList) {
            System.out.println(task);
        }
    }

    private static void addWorkTask(Scanner scanner) throws InvalidTaskDataException {
        System.out.print("Enter Task ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Priority (1-5): ");
        int priority = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter Project Code: ");
        String projectCode = scanner.nextLine();

        tasks.add(new WorkTask(id, title, priority, projectCode));
        System.out.println("Work task added successfully!");
    }

    private static void addPersonalTask(Scanner scanner) throws InvalidTaskDataException {
        System.out.print("Enter Task ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Priority (1-5): ");
        int priority = Integer.parseInt(scanner.nextLine());
        System.out.print("Is this urgent? (true/false): ");
        boolean urgent = Boolean.parseBoolean(scanner.nextLine());

        tasks.add(new PersonalTask(id, title, priority, urgent));
        System.out.println("Personal task added successfully!");
    }

    private static void markTaskCompleted(Scanner scanner) {
        System.out.print("Enter Task ID to complete: ");
        String id = scanner.nextLine();
        for (Task task : tasks) {
            if (task.getId().equalsIgnoreCase(id)) {
                task.setCompleted(true);
                System.out.println("Task marked as completed!");
                return;
            }
        }
        System.out.println("Task ID not found.");
    }

    private static void filterHighPriority() {
        PriorityFilter filter = new PriorityFilter(3);
        List<Task> filtered = new ArrayList<>();
        for (Task task : tasks) {
            if (filter.matches(task)) {
                filtered.add(task);
            }
        }
        System.out.println("\n--- High Priority Tasks (Priority >= 3) ---");
        viewTasks(filtered);
    }
}
