package model;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    public void shouldBeEqualsWhenTaskIDEquals() {
        Task task1 = new Task("Task1", "Task1",
                Status.NEW, Duration.ofMinutes(10), "12.04.24 - 17:40");
        Task task2 = new Task("Task1", "Task1",
                Status.NEW, Duration.ofMinutes(10), "12.04.24 - 17:10");
        task2.setID(task1.getID());
        assertEquals(task1,task2,"Задачи не совпадают");
    }
}