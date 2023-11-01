package controllers;

import model.*;
import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasksList = new HashMap<>();
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
        historyManager.addToHistory(task);
        if (tasksList.containsKey(taskID)){
            return tasksList.get(taskID);
        } else if (epicList.containsKey(taskID)){
            return epicList.get(taskID);
        } else if (subTaskList.containsKey(taskID)){
            return subTaskList.get(taskID);
        }
        return null;
    }

    @Override
    public int makeIDTask(){
        return id +=1;
    }


    @Override
    public void createTask(Task task){
        tasksList.put(task.getID(),task);
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
        return new ArrayList<>(tasksList.values());
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
        tasksList.clear();
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

    @Override
    public void removeTask(Task task){
        int taskID = getTaskID(task);
        if (tasksList.containsKey(taskID)){
            tasksList.remove(taskID);
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
        tasksList.replace(taskID,newTask);
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
