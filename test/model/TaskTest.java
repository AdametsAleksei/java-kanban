package model;

import model.Status;
import model.Task;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    public void shouldBeEqualsWhenTaskIDEquals() {
        Task task1 = new Task("Task1", "Task1", Status.NEW);
        Task task2 = new Task("Task1", "Task1", Status.NEW);
        task2.setID(task1.getID());
        assertEquals(task1,task2,"Задачи не совпадают");
    }
}