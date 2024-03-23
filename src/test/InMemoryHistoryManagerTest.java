package test;

import controllers.InMemoryTaskManager;
import jdk.jfr.StackTrace;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    static InMemoryTaskManager manager;

    @BeforeEach
    public void createManager(){
        manager = new InMemoryTaskManager();
    }

    @Test
    public void ifThreeTaskReceivedSeveralTimesHistoryListShouldBeContainThreeRecordInRightOrder(){
        Epic epic1 = new Epic("Epic 1", "Description for Epic 1");
        manager.createEpic(epic1);
        SubTask subTask1 = new SubTask("SubTask 1", "SubTask 1 for Epic 1",
                Status.IN_PROGRESS,epic1.getID());
        manager.createSubTask(subTask1);
        Task task1 = new Task("Task 1", "Description for Task 1", Status.NEW);
        manager.createTask(task1);
        manager.getTaskFromID(task1.getID());
        manager.getTaskFromID(epic1.getID());
        manager.getTaskFromID(subTask1.getID());
        manager.getTaskFromID(epic1.getID());
        manager.getTaskFromID(epic1.getID());
        manager.getTaskFromID(task1.getID());
        ArrayList<Task> rightOrderList = new ArrayList<>();
        rightOrderList.add(subTask1);
        rightOrderList.add(epic1);
        rightOrderList.add(task1);
        assertEquals(rightOrderList, manager.getHistory());
    }
    @Test
    public void ifTwoTaskReceivedSeveralTimesHistoryListShouldBeContainTwoRecordInRightOrder(){
        Task task1 = new Task("Task 1", "Description for Task 1", Status.NEW);
        manager.createTask(task1);
        Task task2 = new Task("Task 2", "Description for Task 2", Status.DONE);
        manager.createTask(task2);
        manager.getTaskFromID(task1.getID());
        manager.getTaskFromID(task2.getID());
        manager.getTaskFromID(task1.getID());
        ArrayList<Task> rightOrderList = new ArrayList<>();
        rightOrderList.add(task2);
        rightOrderList.add(task1);
        assertEquals(rightOrderList, manager.getHistory());
    }

    @Test
    public void ifOneTaskReceivedSeveralTimesHistoryListShouldBeContainOneRecord(){
        Task task1 = new Task("Task 1", "Description for Task 1", Status.NEW);
        manager.createTask(task1);
        manager.getTaskFromID(task1.getID());
        manager.getTaskFromID(task1.getID());
        manager.getTaskFromID(task1.getID());
        assertEquals(1, manager.getHistory().size());
    }

    @Test
    public void historyListShouldBeEmptyWhenAllTaskDeleted(){
        Task task1 = new Task("Task 1", "Description for Task 1", Status.NEW);
        manager.createTask(task1);
        manager.getTaskFromID(task1.getID());
        Task task2 = new Task("Task 2", "Description for Task 2", Status.DONE);
        manager.createTask(task2);
        manager.getTaskFromID(task2.getID());
        Epic epic1 = new Epic("Epic 1", "Description for Epic 1");
        manager.createEpic(epic1);
        manager.getTaskFromID(epic1.getID());
        manager.removeTask(task1);
        manager.removeTask(task2);
        manager.removeTask(epic1);
        assertTrue(manager.getHistory().isEmpty());
    }

    @Test
    public void removeLastReceivedTask(){
        ArrayList<Task> listTask = new ArrayList<>();
        Task task1 = new Task("Task 1", "Description for Task 1", Status.NEW);
        manager.createTask(task1);
        manager.getTaskFromID(task1.getID());
        listTask.add(task1);
        Task task2 = new Task("Task 2", "Description for Task 2", Status.DONE);
        manager.createTask(task2);
        manager.getTaskFromID(task2.getID());
        listTask.add(task2);
        Epic epic1 = new Epic("Epic 1", "Description for Epic 1");
        manager.createEpic(epic1);
        manager.getTaskFromID(epic1.getID());
        listTask.add(epic1);
        manager.removeTask(task2);
        listTask.remove(task2);
        assertEquals(listTask, manager.getHistory());
    }

    @Test
    public void taskShouldBeRemovedFromHistoryListMoreThenThree(){
        ArrayList<Task> listTask = new ArrayList<>();
        Task task1 = new Task("Task 1", "Description for Task 1", Status.NEW);
        manager.createTask(task1);
        manager.getTaskFromID(task1.getID());
        listTask.add(task1);
        Task task2 = new Task("Task 2", "Description for Task 2", Status.DONE);
        manager.createTask(task2);
        manager.getTaskFromID(task2.getID());
        listTask.add(task2);
        Epic epic1 = new Epic("Epic 1", "Description for Epic 1");
        manager.createEpic(epic1);
        manager.getTaskFromID(epic1.getID());
        listTask.add(epic1);
        manager.removeTask(task2);
        listTask.remove(task2);
        assertEquals(listTask, manager.getHistory());
    }

    @Test
    public void listTaskAndHistoryListShouldBeEqual(){
        ArrayList<Task> listTask = new ArrayList<>();
        Task task1 = new Task("Task 1", "Description for Task 1", Status.NEW);
        manager.createTask(task1);
        manager.getTaskFromID(task1.getID());
        listTask.add(task1);
        Task task2 = new Task("Task 2", "Description for Task 2", Status.DONE);
        manager.createTask(task2);
        manager.getTaskFromID(task2.getID());
        listTask.add(task2);
        Epic epic1 = new Epic("Epic 1", "Description for Epic 1");
        manager.createEpic(epic1);
        manager.getTaskFromID(epic1.getID());
        listTask.add(epic1);
        SubTask subTask1 = new SubTask("SubTask 1", "SubTask 1 for Epic 1",
                Status.IN_PROGRESS,epic1.getID());
        manager.createSubTask(subTask1);
        manager.getTaskFromID(subTask1.getID());
        listTask.add(subTask1);
        SubTask subTask2 = new SubTask("SubTask 2", "SubTask 2 for Epic 1",
                Status.DONE,epic1.getID());
        manager.createSubTask(subTask2);
        manager.getTaskFromID(subTask2.getID());
        listTask.add(subTask2);
        Epic epic2 = new Epic("Epic 2", "Description for Epic 2");
        manager.createEpic(epic2);
        manager.getTaskFromID(epic2.getID());
        listTask.add(epic2);
        SubTask subTask3 = new SubTask("SubTask 3", "SubTask 3 for Epic 2",
                Status.NEW, epic2.getID());
        manager.createSubTask(subTask3);
        manager.getTaskFromID(subTask3.getID());
        listTask.add(subTask3);
        assertTrue(listTask.equals(manager.getHistory()));
    }

    @Test
    public void shouldBeEqualAddedTaskAndRecordedTask(){
        Task task1 = new Task("Task 1", "Description for Task 1", Status.NEW);
        manager.createTask(task1);
        manager.getTaskFromID(task1.getID());
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