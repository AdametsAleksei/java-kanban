public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task1 = taskManager.createTask("Задача 1", "Описание 1", "NEW");
        Epic epic1 = taskManager.createEpic("Эпик 1", " Эпик 1");
        SubTask subTask1 = taskManager.createSubTask("SubTask 1","for epic 1", "NEW" ,epic1);
        SubTask subTask2 = taskManager.createSubTask("SubTask 2","for epic 1", "NEW", epic1);
        Epic epic2 = taskManager.createEpic("Epic2", "Epic with 0 SubTask");

        System.out.println(taskManager.getTaskFromID(epic1));
        taskManager.removeTask(subTask1);
        System.out.println(taskManager.getTaskFromID(epic1));

        Epic epicUpdate = taskManager.createEpic("EpicUpdate", "Update");
        taskManager.updateTask(epic1,epicUpdate);
    }
}
