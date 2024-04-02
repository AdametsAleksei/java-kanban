import controllers.*;
import model.*;

public class Main {

    public static void main(String[] args) {
        FileBackedTaskManager manager = Managers.getDefaultFile();
        //Заведите несколько разных задач, эпиков и подзадач.
        Task task1 = new Task("Task 1", "Description for Task 1", Status.NEW);
        manager.createTask(task1);
        Task task2 = new Task("Task 2", "Description for Task 2", Status.DONE);
        manager.createTask(task2);
        Epic epic1 = new Epic("Epic 1", "Epic with 3 SubTask");
        manager.createEpic(epic1);
        SubTask subTask1 = new SubTask("SubTask 1", "SubTask 1 for Epic 1",
                Status.IN_PROGRESS,epic1.getID());
        manager.createSubTask(subTask1);
        SubTask subTask2 = new SubTask("SubTask 2", "SubTask 2 for Epic 1",
                Status.DONE,epic1.getID());
        manager.createSubTask(subTask2);
        SubTask subTask3 = new SubTask("SubTask 3", "SubTask 3 for Epic 1",
                Status.DONE,epic1.getID());
        manager.createSubTask(subTask3);
        Epic epic2 = new Epic("Epic 2", "Epic without SubTask");
        manager.createEpic(epic2);
        manager.getTaskFromID(task1.getID());
        manager.getTaskFromID(task1.getID());
        manager.getTaskFromID(epic1.getID());
        manager.getTaskFromID(epic2.getID());
        manager.getTaskFromID(subTask1.getID());
        manager.getTaskFromID(epic1.getID());
        manager.removeTask(subTask1);
        //Создайте новый FileBackedTaskManager-менеджер из этого же файла.
        FileBackedTaskManager managerNew = Managers.getDefaultFile();
        managerNew.loadFromFile();
        //Проверьте, что все задачи, эпики, подзадачи, которые были в старом менеджере, есть в новом.
        System.out.println(manager.getHistory());
        System.out.println(managerNew.getHistory());
        System.out.println(manager.getAllTask());
        System.out.println(managerNew.getAllTask());
        System.out.println(manager.getAllEpic());
        System.out.println(managerNew.getAllEpic());
        System.out.println(manager.getAllSubTask());
        System.out.println(managerNew.getAllSubTask());
    }
}
