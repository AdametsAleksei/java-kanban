public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = new TaskManager();
        Task task1 = taskManager.createTask("Задача 1", "Описание 1", "NEW");
        Task task2 = taskManager.createTask("Задача 2", "Описание 2", "NEW");
        Task task3 = taskManager.createTask("Задача 3", "Описание 3", "NEW");
        System.out.println(taskManager.getTaskID(task1));
        System.out.println(taskManager.getTaskID(task2));
        System.out.println(taskManager.getTaskID(task3));

        System.out.println(taskManager.getTasksList());
    }
}
