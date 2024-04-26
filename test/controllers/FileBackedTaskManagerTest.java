package controllers;

import model.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class FileBackedTaskManagerTest {

    File file;

    @BeforeEach
    public void createTestFile() throws IOException {
        file = File.createTempFile("test", "csv");
    }

    @Test
    public void historyFromManagerAndFromFileShouldBeEquals() {
        FileBackedTaskManager manager = Managers.getDefaultFile(file);
        Epic epic1 = new Epic("Epic 1", "Description for Epic 1");
        manager.createEpic(epic1);
        SubTask subTask1 = new SubTask("SubTask 1", "SubTask 1 for Epic 1",
                Status.IN_PROGRESS,epic1.getID().getAsInt(), Duration.ofMinutes(30), "12.04.24 - 17:40");
        manager.createSubTask(subTask1);
        Task task1 = new Task("Task 1", "Description for Task 1",
                Status.NEW, Duration.ofMinutes(10), "12.04.24 - 17:10");
        manager.createTask(task1);
        manager.getTaskFromID(task1.getID().getAsInt());
        manager.getTaskFromID(epic1.getID().getAsInt());
        manager.getTaskFromID(subTask1.getID().getAsInt());
        manager.removeTask(task1);
        manager.getTaskFromID(epic1.getID().getAsInt());
        manager.getTaskFromID(epic1.getID().getAsInt());
        manager.getTaskFromID(task1.getID().getAsInt());
        FileBackedTaskManager manager1 = Managers.getDefaultFile(file);
        assertEquals(manager.getHistory(), manager1.getHistory());
    }

    @Test
    public void shouldBeEmptyFileWhenAllDataDeleted() {
        FileBackedTaskManager manager = Managers.getDefaultFile(file);
        Task task1 = new Task("Task 1", "Description for Task 1",
                Status.NEW, Duration.ofMinutes(20), "12.04.24 - 17:40");
        manager.createTask(task1);
        Epic epic1 = new Epic("Epic 1", "Epic with 3 SubTask");
        manager.createEpic(epic1);
        SubTask subTask1 = new SubTask("SubTask 2", "SubTask 2 for Epic 1",
                Status.DONE,epic1.getID().getAsInt(), Duration.ofMinutes(10), "12.04.24 - 17:20");
        manager.createSubTask(subTask1);
        manager.deleteAllTask();
        manager.deleteAllSubTask();
        manager.deleteAllEpic();
        FileBackedTaskManager manager1 = Managers.getDefaultFile(file);
        assertEquals(manager.getAllTask(), manager1.getAllTask());
        assertEquals(manager.getAllEpic(), manager1.getAllSubTask());
        assertEquals(manager.getAllEpic(), manager1.getAllEpic());
    }

    @Test
    public void shouldBeEqualsWhenOneTaskSaveToFileAndLoadFromFile() {
        FileBackedTaskManager manager = Managers.getDefaultFile(file);
        Task task1 = new Task("Task 1", "Description for Task 1",
                Status.NEW, Duration.ofMinutes(20), "12.04.24 - 17:40");
        manager.createTask(task1);
        FileBackedTaskManager manager1 = Managers.getDefaultFile(file);
        assertEquals(manager.getAllTask(), manager1.getAllTask());
    }

    @Test
    public void shouldBeEqualsWhenOneEpicAndOneSubTaskSaveToFileAndLoadFromFile() {
        FileBackedTaskManager manager = Managers.getDefaultFile(file);
        Epic epic1 = new Epic("Epic 1", "Epic with 3 SubTask");
        manager.createEpic(epic1);
        SubTask subTask1 = new SubTask("SubTask 2", "SubTask 2 for Epic 1",
                Status.DONE,epic1.getID().getAsInt(), Duration.ofMinutes(20), "12.04.24 - 17:40");
        manager.createSubTask(subTask1);
        FileBackedTaskManager manager1 = Managers.getDefaultFile(file);
        assertEquals(manager.getAllEpic(), manager1.getAllEpic());
        assertEquals(manager.getAllSubTask(), manager1.getAllSubTask());
    }

}
