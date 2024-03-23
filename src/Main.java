import controllers.*;
import model.*;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();
        //Создайте две задачи, эпик с тремя подзадачами и эпик без подзадач.
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
        SubTask subTask3 = new SubTask("SubTask 2", "SubTask 3 for Epic 1",
                Status.DONE,epic1.getID());
        manager.createSubTask(subTask3);
        Epic epic2 = new Epic("Epic 2", "Epic without SubTask");
        manager.createEpic(epic2);
        //Запросите созданные задачи несколько раз в разном порядке.
        //После каждого запроса выведите историю и убедитесь, что в ней нет повторов.
        manager.getTaskFromID(task1.getID());
        System.out.println(manager.getHistory());
        manager.getTaskFromID(task1.getID());
        System.out.println(manager.getHistory());
        manager.getTaskFromID(epic1.getID());
        manager.getTaskFromID(epic2.getID());
        manager.getTaskFromID(subTask1.getID());
        manager.getTaskFromID(epic1.getID());
        System.out.println(manager.getHistory());
        //Удалите задачу, которая есть в истории, и проверьте, что при печати она не будет выводиться.
        manager.removeTask(subTask1);
        System.out.println(manager.getHistory());
        //Удалите эпик с тремя подзадачами и убедитесь, что из истории удалился как сам эпик, так и все его подзадачи.
        manager.getTaskFromID(subTask3.getID());
        manager.getTaskFromID(subTask2.getID());
        manager.getTaskFromID(epic1.getID());
        System.out.println(manager.getHistory());
        manager.removeTask(epic1);
        System.out.println(manager.getHistory());
    }
}
