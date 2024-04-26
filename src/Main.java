import controllers.*;
import model.*;

import java.io.File;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.OptionalInt;

public class Main {

    public static void main(String[] args) {
        File file = new File(Paths.get("src",
                "Resources","TaskManager.csv").toString());
        // FileBackedTaskManager manager = Managers.getDefaultFile(file);
        TaskManager manager = Managers.getDefaultFile(file);
        //TaskManager manager = Managers.getDefault();
        //Заведите несколько разных задач, эпиков и подзадач.
        //dd.MM.yy - HH:mm
        Task task1 = new Task("Task 1", "Description for Task 1", Status.NEW,
                Duration.ofMinutes(10), "11.04.24 - 17:40");

        manager.createTask(task1);
        Task task2 = new Task("Task 2", "Description for Task 2", Status.DONE,
                Duration.ofMinutes(10), "12.04.24 - 17:20");
        manager.createTask(task2);
        Epic epic1 = new Epic("Epic 1", "Epic with 3 SubTask");
        manager.createEpic(epic1);
        SubTask subTask1 = new SubTask("SubTask 1", "SubTask 1 for Epic 1",
                Status.IN_PROGRESS,epic1.getID().getAsInt(), Duration.ofMinutes(30), "12.04.24 - 17:50");
        manager.createSubTask(subTask1);
        SubTask subTask2 = new SubTask("SubTask 2", "SubTask 2 for Epic 1",
                Status.DONE,epic1.getID().getAsInt(), Duration.ofMinutes(30), "12.04.24 - 12:30");
        manager.createSubTask(subTask2);
        SubTask subTask3 = new SubTask("SubTask 3", "SubTask 3 for Epic 1",
                Status.DONE,epic1.getID().getAsInt(), Duration.ofMinutes(10), "12.04.24 - 15:40");
        manager.createSubTask(subTask3);
        Epic epic2 = new Epic("Epic 2", "Epic without SubTask");
        manager.createEpic(epic2);
        manager.getTaskFromID(task1.getID().getAsInt());
        manager.getTaskFromID(task1.getID().getAsInt());
        manager.getTaskFromID(epic1.getID().getAsInt());
        manager.getTaskFromID(epic2.getID().getAsInt());
        manager.getTaskFromID(subTask1.getID().getAsInt());
        manager.getTaskFromID(epic1.getID().getAsInt());
        manager.removeTask(subTask2);
        Task taskForReplace = new Task("Task Replaced", "Description for Task 1", Status.NEW,
                Duration.ofMinutes(10), "12.04.24 - 17:00");
        //manager.updateTask(task1.getID().getAsInt(), taskForReplace);
        System.out.println(manager.getPrioritizedTasks());

    }
}