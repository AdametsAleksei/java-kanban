public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        //Создайте 2 задачи, один эпик с 2 подзадачами, а другой эпик с 1 подзадачей.
        Task task1 = taskManager.createTask("Задача 1", "Описание 1", "NEW");
        Task task2 = taskManager.createTask("Task 2", "Description fot task2", "DONE");
        Epic epic1 = taskManager.createEpic("Эпик 1", " Эпик 1");
        SubTask subTask1 = taskManager.createSubTask("SubTask 1","for epic 1", "NEW" ,epic1);
        SubTask subTask2 = taskManager.createSubTask("SubTask 2","for epic 1", "DONE", epic1);
        Epic epic2 = taskManager.createEpic("Epic2", "Epic with 0 SubTask");
        SubTask subTask3 = taskManager.createSubTask("SubTask 3", "for epic 3",
                "IN_PROGRESS", epic2);

        //Распечатайте списки эпиков, задач и подзадач
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getAllTask());
        System.out.println(taskManager.getAllSubTask());
        //Измените статусы созданных объектов, распечатайте. Проверьте, что статус задачи и подзадачи сохранился,
        // а статус эпика рассчитался по статусам подзадач.
        System.out.println(task1);
        taskManager.updateTask(task1.getID(task1), "new Task1", "Change status", "IN_PROGRESS");
        System.out.println(task1);
    }
}
