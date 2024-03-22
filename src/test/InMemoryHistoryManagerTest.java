package test;

import controllers.InMemoryTaskManager;
import model.Epic;
import model.Status;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    static InMemoryTaskManager manager;

    @BeforeEach
    public void createManager(){
        manager = new InMemoryTaskManager();
    }

    @Test
    public void shouldBeReplacedFirstRecordWhenAddedElevenTaskToHistoryManager(){
        Task task1 = new Task("Task 1", "Description for Task 1", Status.NEW);
        Epic epic1 = new Epic("Epic1", "Description for Epic 1");
        manager.createTask(task1);
        manager.createTask(epic1);
        manager.getTaskFromID(task1.getID()); //0 index
        manager.getTaskFromID(epic1.getID()); //1
        manager.getTaskFromID(task1.getID()); //2
        manager.getTaskFromID(task1.getID()); //3
        manager.getTaskFromID(task1.getID()); //4
        manager.getTaskFromID(task1.getID()); //5
        manager.getTaskFromID(task1.getID()); //6
        manager.getTaskFromID(task1.getID()); //7
        manager.getTaskFromID(task1.getID()); //8
        manager.getTaskFromID(task1.getID()); //9
        manager.getTaskFromID(task1.getID());  //->9
        assertEquals(epic1, manager.getHistory().get(0));
    }

    @Test
    public void shouldBeTenWhenAddedElevenOrMoreTaskToHistoryManager(){
        Task task1 = new Task("Task 1", "Description for Task 1", Status.NEW);
        manager.createTask(task1);
        manager.getTaskFromID(task1.getID()); //1 size
        manager.getTaskFromID(task1.getID()); //2
        manager.getTaskFromID(task1.getID()); //3
        manager.getTaskFromID(task1.getID()); //4
        manager.getTaskFromID(task1.getID()); //5
        manager.getTaskFromID(task1.getID()); //6
        manager.getTaskFromID(task1.getID()); //7
        manager.getTaskFromID(task1.getID()); //8
        manager.getTaskFromID(task1.getID()); //9
        manager.getTaskFromID(task1.getID()); //10
        manager.getTaskFromID(task1.getID()); //11
        manager.getTaskFromID(task1.getID()); //12
        manager.getTaskFromID(task1.getID()); //13
        assertEquals(10, manager.getHistory().size());
    }

    @Test
    public void taskShouldNotChangeWhenTaskUpdated(){
        Task task1 = new Task("Task 1", "Description for Task 1", Status.NEW);
        manager.createTask(task1);
        Task oldTask = task1;
        manager.getTaskFromID(task1.getID());
        Task updateTask = new Task("TaskUpdate", "Description for UpdateTask", Status.IN_PROGRESS);
        manager.updateTask(task1.getID(),updateTask);
        assertEquals(oldTask, manager.getHistory().get(0));
    }

    @Test
    public void shouldBeEqualAddedTaskAndRecordedTask(){
        Task task1 = new Task("Task 1", "Description for Task 1", Status.NEW);
        manager.createTask(task1);
        manager.getTaskFromID(task1.getID());
        assertEquals(task1, manager.getHistory().get(0));
    }

    @Test
    public void shouldBeOneRecordWhenTaskGetOneTime(){
        Task task1 = new Task("Task 1", "Description for Task 1", Status.NEW);
        manager.createTask(task1);
        manager.getTaskFromID(task1.getID());
        assertEquals(1, manager.getHistory().size());
    }

    @Test
    public void shouldBeEmptyWhenTaskCreated(){
        Task task1 = new Task("Task 1", "Description for Task 1", Status.NEW);
        manager.createTask(task1);
        assertTrue(manager.getHistory().isEmpty());
    }

    @Test
    public void shouldBeEmptyWhenTaskNotCreated(){
        assertTrue(manager.getHistory().isEmpty());
    }

}