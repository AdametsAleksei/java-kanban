package controllers;

import model.*;
import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> taskList = new HashMap<>();
    private final HashMap<Integer, Epic> epicList = new HashMap<>();
    private final HashMap<Integer, SubTask> subTaskList = new HashMap<>();
    private int id = 0;

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public ArrayList<Task> getHistory(){
        return historyManager.getHistory();
    }

    @Override
    public Task getTaskFromID(Task task){
        int taskID = task.getID();
        if (taskList.containsValue(task)){
            historyManager.addToHistory(task);
            return taskList.get(taskID);
        } else if (epicList.containsValue(task)){
            historyManager.addToHistory(task);
            return epicList.get(taskID);
        } else if (subTaskList.containsValue(task)){
            historyManager.addToHistory(task);
            return subTaskList.get(taskID);
        }
        return null;
    }

    @Override
    public int makeIDTask(){
        return id +=1;
    }

    /*Привет! Тут у меня вопрос возник)
    Твой комментарий:
    "Здесь и для других типов задач тоже.
    Идентификатор не передается от пользователя, но, даже, если передается, мы его не должны учитывать, а он должен
    быть переопределен в любом случае для поддержания уникальности."

    Вопрос:
    У меня при создании задачи или задач другого типа, в методе main вызывается makeIDTask():
    Task task1 = new Task("Task 1", "Description for Task 1", manager.makeIDTask(), Status.NEW);
    который, отвечает как раз за поддержание уникальности, и как я себе это представляю, не передаётся от пользователя
    (вопрос как выглядит пользовательский интерфейс :))
    Или я не совсем правильно представляю и нужно поправить?
    */
    @Override
    public void createTask(Task task){
        taskList.put(task.getID(),task);
    }

    @Override
    public void createEpic(Epic epic){
        epicList.put(epic.getID(),epic);
        epic.setStatus(Status.NEW);
    }

    @Override
    public void createSubTask(SubTask subTask){
        subTaskList.put(subTask.getID(),subTask);
        epicList.get(subTask.getEpicID()).addSubTaskToEpic(subTask);
    }

    @Override
    public ArrayList<Task> getAllTask(){
        return new ArrayList<>(taskList.values());
    }

    @Override
    public ArrayList<Task> getAllSubTask(){
        return new ArrayList<>(subTaskList.values());
    }

    @Override
    public ArrayList<Task> getAllEpic(){
        return new ArrayList<>(epicList.values());
    }

    @Override
    public void deleteAllTask(){
        taskList.clear();
    }

    @Override
    public void deleteAllEpic(){
        epicList.clear();
        deleteAllSubTask();
    }

    @Override
    public void deleteAllSubTask(){
        ArrayList<SubTask> deleteList = new ArrayList<>(subTaskList.values());
        for (SubTask subTask : deleteList) {
            removeTask(subTask);
        }
    }

    //Извиняюсь, недоглядел
    @Override
    public void removeTask(Task task){
        int taskID = getTaskID(task);
        if (taskList.containsKey(taskID)){
            taskList.remove(taskID);
        } else if (epicList.containsKey(taskID)){
            epicList.remove(taskID);
        } else if (subTaskList.containsKey(taskID)){
            Epic epic = epicList.get(subTaskList.get(taskID).getEpicID());
            epic.removeSubTaskFromEpic(subTaskList.get(taskID));
            subTaskList.remove(taskID);
        }
    }

    @Override
    public void updateTask(int taskID, String name, String description, Status status){
        Task newTask = new Task(name, description, taskID, status);
        taskList.replace(taskID,newTask);
    }

    @Override
    public void updateEpic(int epicID, String name, String description){
        Epic newEpic = new Epic(name, description, epicID);
        newEpic.updateStatus();
        for(SubTask subTask : subTaskList.values()){
            if (subTask.getEpicID() == epicID){
                newEpic.addSubTaskToEpic(subTask);
            }
        }
        epicList.replace(epicID,newEpic);
    }

    @Override
    public void updateSubTask(int taskID, Epic epic, String name, String description, Status status){
        SubTask subTask = new SubTask(name, description, status, taskID, epic.getID());
        epic.removeSubTaskFromEpic(subTaskList.get(taskID));
        subTaskList.replace(taskID,subTask);
        epic.addSubTaskToEpic(subTask);
    }

    @Override
    public void setStatusTask(Task task, Status newStatus){
        task.setStatus(newStatus);
    }

    @Override
    public void setStatusSubTask(SubTask subTask, Status newStatus){
        subTask.setStatus(newStatus);
        epicList.get(subTask.getEpicID()).updateStatus();
    }

    @Override
    public ArrayList<Task> getSubTasksFromEpic(Epic epic){
        return epic.getListSubTasks();
    }

    @Override
    public int getTaskID(Task task){
        return task.getID();
    }

}
