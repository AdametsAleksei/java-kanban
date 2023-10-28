package controllers;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasksList = new HashMap<>();
    private HashMap<Integer, Epic> epicList = new HashMap<>();
    private HashMap<Integer, SubTask> subTaskList = new HashMap<>();
    private int id = 0;

    public int makeIDTask(){
        return id +=1;
    }

    public void createTask(Task task){
        tasksList.put(task.getID(),task);
    }

    public void createEpic(Epic epic){
        epicList.put(epic.getID(),epic);
        epic.setStatus("NEW");
    }

    //Вызов этого метода нужно перенести в метод “добавление подзадачи”.
    //в addSubTaskToEpic() тоже "зашито" обновление статуса эпика)
    public void createSubTask(SubTask subTask){
        subTaskList.put(subTask.getID(),subTask);
        epicList.get(subTask.getEpicID()).addSubTaskToEpic(subTask);
    }

    public ArrayList getAllTask(){
        return new ArrayList<>(tasksList.values());
    }

    public ArrayList getAllSubTask(){
        return new ArrayList<>(subTaskList.values());
    }

    public ArrayList getAllEpic(){
        return new ArrayList<>(epicList.values());
    }

    public void deleteAllTask(){
        tasksList.clear();
    }

    public void deleteAllEpic(){
        epicList.clear();
        deleteAllSubTask();
    }

    public void deleteAllSubTask(){
        ArrayList<SubTask> deleteList = new ArrayList<>(subTaskList.values());
        for (SubTask subTask : deleteList) {
            removeTask(subTask);
        }
    }

    public Task getTaskFromID(Task task){
        int taskID = task.getID();
        if (tasksList.containsKey(taskID)){
            return tasksList.get(taskID);
        } else if (epicList.containsKey(taskID)){
            return epicList.get(taskID);
        } else if (subTaskList.containsKey(taskID)){
            return subTaskList.get(taskID);
        }
        return null;
    }

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

    public void updateTask(int taskID, String name, String description, String status){
        Task newTask = new Task(name, description, taskID, status);
        tasksList.replace(taskID,newTask);
    }

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

    public void updateSubTask(int taskID, Epic epic, String name, String description, String status){
        SubTask subTask = new SubTask(name, description, status, taskID, epic.getID());
        epic.removeSubTaskFromEpic(subTaskList.get(taskID));
        subTaskList.replace(taskID,subTask);
        epic.addSubTaskToEpic(subTask);
    }

    public void setStatusTask(Task task, String newStatus){
        task.setStatus(newStatus);
    }

    public void setStatusSubTask(SubTask subTask, String newStatus){
        subTask.setStatus(newStatus);
        epicList.get(subTask.getEpicID()).updateStatus();
    }

    public ArrayList getSubTasksFromEpic(Epic epic){
        return epic.getListSubTasks();
    }

    public int getTaskID(Task task){
        return task.getID();
    }

}
