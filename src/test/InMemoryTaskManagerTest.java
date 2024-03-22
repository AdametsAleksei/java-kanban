package test;

import controllers.InMemoryHistoryManager;
import controllers.InMemoryTaskManager;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    static InMemoryTaskManager manager;

    @BeforeEach
    public void inMemoryTaskManagerCreate() {
        manager = new InMemoryTaskManager();
    }

    @Test
    public void subTaskShouldBeUpdated(){
        Epic epic1 = new Epic("Epic 1", "Description for Epic 1");
        manager.createEpic(epic1);
        SubTask subTask1 = new SubTask("SubTask 1", "SubTask 1 for Epic 1",
                Status.IN_PROGRESS, epic1.getID());
        manager.createSubTask(subTask1);
        SubTask updateSubTask = new SubTask("SubTaskUpdate", "Description for UpdateSubTask",
                Status.NEW, epic1.getID());
        manager.updateSubTask(subTask1.getID(),updateSubTask);
        assertEquals(updateSubTask.getName(), manager.getTaskFromID(subTask1.getID()).getName());
        assertEquals(updateSubTask.getDescription(), manager.getTaskFromID(subTask1.getID()).getDescription());
        assertEquals(updateSubTask.getStatus(), manager.getTaskFromID(subTask1.getID()).getStatus());
        assertEquals(updateSubTask, manager.getTaskFromID(subTask1.getID()));
    }

    @Test
    public void epicShouldBeUpdated(){
        Epic epic1 = new Epic("Epic 1", "Description for Epic 1");
        manager.createEpic(epic1);
        Epic updateEpic = new Epic("EpicUpdate", "Description for UpdateEpic");
        manager.updateEpic(epic1.getID(),updateEpic);
        assertEquals(updateEpic.getName(), manager.getTaskFromID(epic1.getID()).getName());
        assertEquals(updateEpic.getDescription(), manager.getTaskFromID(epic1.getID()).getDescription());
        assertEquals(updateEpic.getStatus(), manager.getTaskFromID(epic1.getID()).getStatus());
        assertEquals(updateEpic, manager.getTaskFromID(epic1.getID()));
    }


    @Test
    public void taskShouldBeUpdated(){
        Task task1 = new Task("Task 1", "Description for Task 1", Status.NEW);
        manager.createTask(task1);
        Task updateTask = new Task("TaskUpdate", "Description for UpdateTask", Status.IN_PROGRESS);
        manager.updateTask(task1.getID(),updateTask);
        assertEquals(updateTask.getName(), manager.getTaskFromID(task1.getID()).getName());
        assertEquals(updateTask.getDescription(), manager.getTaskFromID(task1.getID()).getDescription());
        assertEquals(updateTask.getStatus(), manager.getTaskFromID(task1.getID()).getStatus());
        assertEquals(updateTask, manager.getTaskFromID(task1.getID()));
    }

    @Test
    public void listSubTaskShouldBeEmptyWhenAllSubTaskDeleted(){
        Epic epic1 = new Epic("Epic 1", "Description for Epic 1");
        manager.createEpic(epic1);
        SubTask subTask1 = new SubTask("SubTask 1", "SubTask 1 for Epic 1",
                Status.IN_PROGRESS,epic1.getID());
        manager.createSubTask(subTask1);
        SubTask subTask2 = new SubTask("SubTask 2", "SubTask 2 for Epic 1",
                Status.DONE,epic1.getID());
        manager.createSubTask(subTask2);
        manager.deleteAllSubTask();
        assertTrue(manager.getAllSubTask().isEmpty());
    }

    @Test
    public void listEpicAndSubTaskShouldBeEmptyWhenAllEpicDeleted(){
        Epic epic1 = new Epic("Epic 1", "Description for Epic 1");
        manager.createEpic(epic1);
        manager.deleteAllEpic();
        assertTrue(manager.getAllEpic().isEmpty());
        assertTrue(manager.getSubTasksFromEpic(epic1).isEmpty());
    }

    @Test
    public void listTaskShouldBeEmptyWhenAllTaskDeleted(){
        Task task1 = new Task("Task 1", "Description for Task 1", Status.NEW);
        manager.createTask(task1);
        Task task2 = new Task("Task 2", "Description for Task 2", Status.DONE);
        manager.createTask(task2);
        manager.deleteAllTask();
        assertTrue(manager.getAllTask().isEmpty());
    }

    @Test
    public void sumEpicShouldBeEqual(){
        Epic epic1 = new Epic("Epic 1", "Description for Epic 1");
        manager.createEpic(epic1);
        assertEquals(1, manager.getAllEpic().size());
    }

    @Test
    public void sumSubTaskShouldBeEqual(){
        Epic epic1 = new Epic("Epic 1", "Description for Epic 1");
        manager.createEpic(epic1);
        SubTask subTask1 = new SubTask("SubTask 1", "SubTask 1 for Epic 1",
                Status.IN_PROGRESS,epic1.getID());
        manager.createSubTask(subTask1);
        SubTask subTask2 = new SubTask("SubTask 2", "SubTask 2 for Epic 1",
                Status.DONE,epic1.getID());
        manager.createSubTask(subTask2);
        assertEquals(2, manager.getAllSubTask().size());
    }

    @Test
    public void sumTaskShouldBeEqual(){
        Task task1 = new Task("Task 1", "Description for Task 1", Status.NEW);
        manager.createTask(task1);
        Task task2 = new Task("Task 2", "Description for Task 2", Status.DONE);
        manager.createTask(task2);
        assertEquals(2, manager.getAllTask().size());
    }

    @Test
    public void statusSubTaskShouldBeChanged(){
        Epic epic1 = new Epic("Epic 1", "Description for Epic 1");
        manager.createEpic(epic1);
        SubTask subTask1 = new SubTask("SubTask 1", "SubTask 1 for Epic 1",
                Status.DONE, epic1.getID());
        manager.createSubTask(subTask1);
        manager.setStatusSubTask(subTask1,Status.NEW);
        assertEquals(Status.NEW, manager.getTaskFromID(manager.getTaskID(subTask1)).getStatus());
    }

    @Test
    public void statusEpicShouldBeNewWhenDeletedAllSubTask(){
        Epic epic1 = new Epic("Epic 1", "Description for Epic 1");
        manager.createEpic(epic1);
        SubTask subTask1 = new SubTask("SubTask 1", "SubTask 1 for Epic 1",
                Status.DONE, epic1.getID());
        manager.createSubTask(subTask1);
        SubTask subTask2 = new SubTask("SubTask 2", "SubTask 2 for Epic 1",
                Status.NEW, epic1.getID());
        manager.createSubTask(subTask2);
        manager.deleteAllSubTask();
        assertEquals(Status.NEW, manager.getTaskFromID(manager.getTaskID(epic1)).getStatus());
    }

    @Test
    public void statusEpicShouldBeNewWhenStatusAllSubTaskNew(){
        Epic epic1 = new Epic("Epic 1", "Description for Epic 1");
        manager.createEpic(epic1);
        SubTask subTask1 = new SubTask("SubTask 1", "SubTask 1 for Epic 1",
                Status.NEW, epic1.getID());
        manager.createSubTask(subTask1);
        SubTask subTask2 = new SubTask("SubTask 2", "SubTask 2 for Epic 1",
                Status.NEW, epic1.getID());
        manager.createSubTask(subTask2);
        assertEquals(Status.NEW, manager.getTaskFromID(manager.getTaskID(epic1)).getStatus());
    }

    @Test
    public void statusEpicShouldBeInProgressWhenStatusAllSubTaskNotDoneAndNotNew(){
        Epic epic1 = new Epic("Epic 1", "Description for Epic 1");
        manager.createEpic(epic1);
        SubTask subTask1 = new SubTask("SubTask 1", "SubTask 1 for Epic 1",
                Status.DONE, epic1.getID());
        manager.createSubTask(subTask1);
        SubTask subTask2 = new SubTask("SubTask 2", "SubTask 2 for Epic 1",
                Status.NEW, epic1.getID());
        manager.createSubTask(subTask2);
        assertEquals(Status.IN_PROGRESS, manager.getTaskFromID(manager.getTaskID(epic1)).getStatus());
    }

    @Test
    public void statusEpicShouldBeDoneWhenStatusAllSubTaskDone(){
        Epic epic1 = new Epic("Epic 1", "Description for Epic 1");
        manager.createEpic(epic1);
        SubTask subTask1 = new SubTask("SubTask 1", "SubTask 1 for Epic 1",
                Status.DONE, epic1.getID());
        manager.createSubTask(subTask1);
        SubTask subTask2 = new SubTask("SubTask 2", "SubTask 2 for Epic 1",
                Status.DONE, epic1.getID());
        manager.createSubTask(subTask2);
        assertEquals(Status.DONE, manager.getTaskFromID(manager.getTaskID(epic1)).getStatus());
    }

    @Test
    public void statusShouldBeNewWhenEpicCreated(){
        Epic epic1 = new Epic("Epic 1", "Description for Epic 1");
        manager.createEpic(epic1);
        assertEquals(Status.NEW, manager.getTaskFromID(manager.getTaskID(epic1)).getStatus());
    }

    @Test
    public void statusTaskShouldBeChanged(){
        Task task1 = new Task("Task 1", "Description for Task 1", Status.NEW);
        manager.createTask(task1);
        Status newStatus = Status.DONE;
        manager.setStatusTask(task1,newStatus);
        assertEquals(newStatus, manager.getTaskFromID(manager.getTaskID(task1)).getStatus());
    }

    @Test
    public void allFieldsShouldBeEqualWhenSubTaskCreatedByInMemoryTaskManager(){
        String nameEpic = "Epic 1";
        String descriptionEpic = "Description for Epic 1";
        Epic epic1 = new Epic(nameEpic, descriptionEpic);
        manager.createEpic(epic1);
        String name = "SubTask 1";
        String description = "Description for SubTask 1";
        Status status = Status.IN_PROGRESS;
        SubTask subTask1 = new SubTask(name, description,
                status, epic1.getID());
        manager.createSubTask(subTask1);
        assertEquals(name, manager.getTaskFromID(manager.getTaskID(subTask1)).getName());
        assertEquals(description, manager.getTaskFromID(manager.getTaskID(subTask1)).getDescription());
        assertEquals(status, manager.getTaskFromID(manager.getTaskID(subTask1)).getStatus());
    }

    @Test
    public void allFieldsShouldBeEqualWhenEpicCreatedByInMemoryTaskManager(){
        String name = "Epic 1";
        String description = "Description for Epic 1";
        Epic epic1 = new Epic(name, description);
        manager.createEpic(epic1);
        assertEquals(name, manager.getTaskFromID(manager.getTaskID(epic1)).getName());
        assertEquals(description, manager.getTaskFromID(manager.getTaskID(epic1)).getDescription());
    }

    @Test
    public void allFieldsShouldBeEqualWhenTaskCreatedByInMemoryTaskManager(){
        String name = "Task 1";
        String description = "Description for Task 1";
        Status status = Status.NEW;
        Task task1 = new Task(name, description, status);
        manager.createTask(task1);
        assertEquals(name, manager.getTaskFromID(manager.getTaskID(task1)).getName());
        assertEquals(description, manager.getTaskFromID(manager.getTaskID(task1)).getDescription());
        assertEquals(status, manager.getTaskFromID(manager.getTaskID(task1)).getStatus());
    }

    @Test
    public void shouldBeCreatedSubTaskAndFindByID(){
        Epic epic1 = new Epic("Epic 1", "Description for Epic 1");
        manager.createEpic(epic1);
        SubTask subTask1 = new SubTask("SubTask 1", "SubTask 1 for Epic 1",
                Status.IN_PROGRESS, epic1.getID());
        manager.createSubTask(subTask1);
        assertEquals(subTask1.getID(),manager.getTaskID(subTask1));
    }

    @Test
    public void shouldBeCreatedEpicAndFindByID(){
        Epic epic1 = new Epic("Epic 1", "Description for Epic 1");
        manager.createEpic(epic1);
        assertEquals(epic1.getID(), manager.getTaskID(epic1));
    }

    @Test
    public void shouldBeCreatedTaskAndFindByID(){
        Task task1 = new Task("Task 1", "Description for Task 1", Status.NEW);
        manager.createTask(task1);
        assertEquals(task1.getID(), manager.getTaskID(task1));
    }

    @Test
    public void shouldBeReturnedEmptyListWhenEpicNotCreated(){
        assertTrue(manager.getAllEpic().isEmpty());
    }

    @Test
    public void shouldBeReturnedEmptyListWhenSubTaskNotCreated(){
        assertTrue(manager.getAllSubTask().isEmpty());
    }

    @Test
    public void shouldBeReturnedEmptyListWhenTaskNotCreated(){
        assertTrue(manager.getAllTask().isEmpty());
    }

}