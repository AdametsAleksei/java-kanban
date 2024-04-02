package controllers;

import model.*;
import java.util.ArrayList;

public interface TaskManager {

    ArrayList<Task> getHistory();

    int makeIDTask();

    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubTask(SubTask subTask);

    ArrayList<Task> getAllTask();

    ArrayList<Task> getAllSubTask();

    ArrayList<Task> getAllEpic();

    void deleteAllTask();

    void deleteAllEpic();

    void deleteAllSubTask();

    Task getTaskFromID(int taskID);

    void removeTask(Task task);

    void updateTask(int taskID, Task newTask);

    void updateEpic(int epicID, Epic newEpic);

    void updateSubTask(int taskID, SubTask newSubtask);

    void setStatusTask(Task task, Status newStatus);

    void setStatusSubTask(SubTask subTask, Status newStatus);

    ArrayList<Task> getSubTasksFromEpic(Epic epic);

    int getTaskID(Task task);

}
