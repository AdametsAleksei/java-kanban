package controllers;

import model.*;
import java.util.ArrayList;

public interface TaskManager {

    ArrayList getHistory();

    int makeIDTask();

    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubTask(SubTask subTask);

    ArrayList getAllTask();

    ArrayList getAllSubTask();

    ArrayList getAllEpic();

    void deleteAllTask();

    void deleteAllEpic();

    void deleteAllSubTask();

    Task getTaskFromID(Task task);

    void removeTask(Task task);

    void updateTask(int taskID, String name, String description, Status status);

    void updateEpic(int epicID, String name, String description);

    void updateSubTask(int taskID, Epic epic, String name, String description, Status status);

    void setStatusTask(Task task, Status newStatus);

    void setStatusSubTask(SubTask subTask, Status newStatus);

    ArrayList getSubTasksFromEpic(Epic epic);

    int getTaskID(Task task);
}
