import controllers.*;
import model.*;

// Спасибо за развернутый комментарий, вроде всё понял :)

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();
        //Создайте 2 задачи, один эпик с 2 подзадачами, а другой эпик с 1 подзадачей.
        Task task1 = new Task("Task 1", "Description for Task 1", Status.NEW);
        manager.createTask(task1);
        Task task2 = new Task("Task 2", "Description for Task 2", Status.DONE);
        manager.createTask(task2);
        Epic epic1 = new Epic("Epic 1", "Description for Epic 1");
        manager.createEpic(epic1);
        SubTask subTask1 = new SubTask("SubTask 1", "SubTask 1 for Epic 1",
                Status.IN_PROGRESS,epic1.getID());
        manager.createSubTask(subTask1);
        SubTask subTask2 = new SubTask("SubTask 2", "SubTask 2 for Epic 1",
                Status.DONE,epic1.getID());
        manager.createSubTask(subTask2);
        Epic epic2 = new Epic("Epic 2", "Description for Epic 2");
        manager.createEpic(epic2);
        SubTask subTask3 = new SubTask("SubTask 3", "SubTask 3 for Epic 2",
                Status.NEW, epic2.getID());
        manager.createSubTask(subTask3);

        /* ТЗ - 3 (предыдущее)
        //Распечатайте списки эпиков, задач и подзадач
        System.out.println(manager.getAllEpic());
        System.out.println(manager.getAllTask());
        System.out.println(manager.getAllSubTask());
        //Измените статусы созданных объектов, распечатайте. Проверьте, что статус задачи и подзадачи сохранился,
        //а статус эпика рассчитался по статусам подзадач.
        System.out.println(task1);
        manager.setStatusTask(task1,Status.DONE);
        System.out.println(task1);
        System.out.println(epic2);
        System.out.println(subTask3);
        manager.setStatusSubTask(subTask3,Status.DONE);
        System.out.println(epic2);
        System.out.println(subTask3);
        manager.updateTask(task1.getID(),"Task Update", "A",Status.IN_PROGRESS);
        */

        /*//ТЗ - 4 (текущее)
        manager.getTaskFromID(epic1.getID());
        manager.getTaskFromID(subTask1.getID());
        manager.getTaskFromID(task2.getID());
        manager.getTaskFromID(epic1.getID());
        manager.getTaskFromID(epic1.getID());
        manager.getTaskFromID(epic1.getID());
        manager.getTaskFromID(epic1.getID());
        manager.getTaskFromID(task1.getID());
        manager.getTaskFromID(epic1.getID());
        manager.getTaskFromID(task1.getID());
        manager.getTaskFromID(epic1.getID());
        //manager.getTaskFromID(task1.getID());
        System.out.println(manager.getHistory());
        */
    }
}
