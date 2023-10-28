import controllers.TaskManager;
import model.Epic;
import model.SubTask;
import model.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        //Создайте 2 задачи, один эпик с 2 подзадачами, а другой эпик с 1 подзадачей.
        Task task1 = new Task("Task 1", "Description for Task 1",
                taskManager.makeIDTask(), "NEW");
        taskManager.createTask(task1);
        Task task2 = new Task("Task 2", "Description for Task 2",
                taskManager.makeIDTask(), "DONE");
        taskManager.createTask(task2);
        Epic epic1 = new Epic("Epic 1", "Description for Epic 1",
                taskManager.makeIDTask());
        taskManager.createEpic(epic1);
        SubTask subTask1 = new SubTask("SubTask 1", "SubTask 1 for Epic 1", "IN_PROGRESS",
                taskManager.makeIDTask(), epic1.getID());
        taskManager.createSubTask(subTask1);
        SubTask subTask2 = new SubTask("SubTask 2", "SubTask 2 for Epic 1", "DONE",
                taskManager.makeIDTask(), epic1.getID());
        taskManager.createSubTask(subTask2);
        Epic epic2 = new Epic("Epic 2", "Description for Epic 2",
                taskManager.makeIDTask());
        taskManager.createEpic(epic2);
        SubTask subTask3 = new SubTask("SubTask 3", "SubTask 3 for Epic 2", "IN_PROGRESS",
                taskManager.makeIDTask(), epic2.getID());
        taskManager.createSubTask(subTask3);

        //Распечатайте списки эпиков, задач и подзадач
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getAllTask());
        System.out.println(taskManager.getAllSubTask());
        //Измените статусы созданных объектов, распечатайте. Проверьте, что статус задачи и подзадачи сохранился,
        // а статус эпика рассчитался по статусам подзадач.
        System.out.println(task1);
        taskManager.setStatusTask(task1,"IN_PROGRESS");
        System.out.println(task1);
        System.out.println(epic2);
        System.out.println(subTask3);
        taskManager.setStatusSubTask(subTask3,"DONE");
        System.out.println(epic2);
        System.out.println(subTask3);
    }
}
