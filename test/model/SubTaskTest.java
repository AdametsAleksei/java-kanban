package model;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {

    @Test
    public void shouldBeEqualsWhenSubTaskIDEquals() {
        SubTask subTask1 = new SubTask("subTask1", "subTask1",
                Status.NEW, 1, Duration.ofMinutes(10), "12.04.24 - 17:40");
        SubTask subTask2 = new SubTask("subTask2", "subTask2",
                Status.NEW, 1, Duration.ofMinutes(10), "12.04.24 - 17:10");
        subTask2.setID(subTask1.getID());
        assertEquals(subTask1,subTask2,"Задачи не совпадают");
    }

    @Test
    public void shouldReturnedEpicID() {
        int epicID = 1;
        SubTask subTask1 = new SubTask("subTask1", "subTask1",
                Status.NEW, epicID, Duration.ofMinutes(10), "12.04.24 - 17:40");
        assertEquals(epicID, subTask1.getEpicID());
    }
}