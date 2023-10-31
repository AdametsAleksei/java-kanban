import controllers.*;
import model.*;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        //Создайте 2 задачи, один эпик с 2 подзадачами, а другой эпик с 1 подзадачей.
        Task task1 = new Task("Task 1", "Description for Task 1",
                inMemoryTaskManager.makeIDTask(), "NEW");
        inMemoryTaskManager.createTask(task1);
        Task task2 = new Task("Task 2", "Description for Task 2",
                inMemoryTaskManager.makeIDTask(), "DONE");
        inMemoryTaskManager.createTask(task2);
        Epic epic1 = new Epic("Epic 1", "Description for Epic 1",
                inMemoryTaskManager.makeIDTask());
        inMemoryTaskManager.createEpic(epic1);
        SubTask subTask1 = new SubTask("SubTask 1", "SubTask 1 for Epic 1", "IN_PROGRESS",
                inMemoryTaskManager.makeIDTask(), epic1.getID());
        inMemoryTaskManager.createSubTask(subTask1);
        SubTask subTask2 = new SubTask("SubTask 2", "SubTask 2 for Epic 1", "DONE",
                inMemoryTaskManager.makeIDTask(), epic1.getID());
        inMemoryTaskManager.createSubTask(subTask2);
        Epic epic2 = new Epic("Epic 2", "Description for Epic 2",
                inMemoryTaskManager.makeIDTask());
        inMemoryTaskManager.createEpic(epic2);
        SubTask subTask3 = new SubTask("SubTask 3", "SubTask 3 for Epic 2", "IN_PROGRESS",
                inMemoryTaskManager.makeIDTask(), epic2.getID());
        inMemoryTaskManager.createSubTask(subTask3);

        //Распечатайте списки эпиков, задач и подзадач
        System.out.println(inMemoryTaskManager.getAllEpic());
        System.out.println(inMemoryTaskManager.getAllTask());
        System.out.println(inMemoryTaskManager.getAllSubTask());
        //Измените статусы созданных объектов, распечатайте. Проверьте, что статус задачи и подзадачи сохранился,
        // а статус эпика рассчитался по статусам подзадач.
        System.out.println(task1);
        inMemoryTaskManager.setStatusTask(task1,"IN_PROGRESS");
        System.out.println(task1);
        System.out.println(epic2);
        System.out.println(subTask3);
        inMemoryTaskManager.setStatusSubTask(subTask3,"DONE");
        System.out.println(epic2);
        System.out.println(subTask3);
    }
}
