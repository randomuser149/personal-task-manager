package com.taskmanager;

import com.taskmanager.exception.InvalidTaskDataException;
import com.taskmanager.model.PersonalTask;
import com.taskmanager.model.WorkTask;
import com.taskmanager.model.Task;
import com.taskmanager.persistence.TaskStorage;
import com.taskmanager.strategy.PriorityFilter;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    @Test
    public void testWorkTaskPriorityScore() throws InvalidTaskDataException {
        WorkTask workTask = new WorkTask("W1", "Fix bug", 3, "PROJ-101");
        assertEquals(6, workTask.calculatePriorityScore());
    }

    @Test
    public void testPersonalTaskPriorityScore() throws InvalidTaskDataException {
        PersonalTask urgentTask = new PersonalTask("P1", "Buy groceries", 2, true);
        assertEquals(5, urgentTask.calculatePriorityScore());
    }

    @Test
    public void testInvalidTaskPriorityThrowsException() {
        assertThrows(InvalidTaskDataException.class, () -> {
            new WorkTask("W2", "Invalid Task", 10, "PROJ-102");
        });
    }

    @Test
    public void testPriorityFilter() throws InvalidTaskDataException {
        WorkTask highPriority = new WorkTask("W3", "High priority task", 4, "PROJ-103");
        WorkTask lowPriority = new WorkTask("W4", "Low priority task", 1, "PROJ-104");

        PriorityFilter filter = new PriorityFilter(3);
        assertTrue(filter.matches(highPriority));
        assertFalse(filter.matches(lowPriority));
    }

    @Test
    public void testAutoIncrementingId() throws InvalidTaskDataException {
        List<Task> testTasks = new ArrayList<>();
        testTasks.add(new WorkTask("1", "First Task", 2, "PROJ-1"));
        testTasks.add(new PersonalTask("2", "Second Task", 3, false));

        TaskStorage storage = new TaskStorage("test_tasks.csv");
        assertEquals("3", storage.getNextAvailableId(testTasks));
    }
}
