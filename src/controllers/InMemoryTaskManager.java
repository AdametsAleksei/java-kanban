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

    protected void addToHistory(Task task) {
        historyManager.addToHistory(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Task getTaskFromID(int taskID) {
        if (taskList.containsKey(taskID)) {
            Task task = taskList.get(taskID);
            addToHistory(task);
            return task;
        } else if (epicList.containsKey(taskID)) {
            Epic epic = epicList.get(taskID);
            addToHistory(epic);
            return epic;
        } else if (subTaskList.containsKey(taskID)) {
            SubTask subTask = subTaskList.get(taskID);
            addToHistory(subTask);
            return subTask;
        }
        return null;
    }

    @Override
    public int makeIDTask() {
        return id += 1;
    }

    @Override
    public void createTask(Task task) {
        if (task.getID() == 0) {
            task.setID(makeIDTask());
        }
        taskList.put(task.getID(),task);
    }

    @Override
    public void createEpic(Epic epic) {
        if (epic.getID() == 0) {
            epic.setID(makeIDTask());
        }
        epicList.put(epic.getID(),epic);
        epic.setStatus(Status.NEW);
    }

    @Override
    public void createSubTask(SubTask subTask) {
        if (subTask.getID() == 0) {
            subTask.setID(makeIDTask());
        }
        subTaskList.put(subTask.getID(),subTask);
        epicList.get(subTask.getEpicID()).addSubTaskToEpic(subTask);
    }

    @Override
    public ArrayList<Task> getAllTask() {
        return new ArrayList<>(taskList.values());
    }

    @Override
    public ArrayList<Task> getAllSubTask() {
        return new ArrayList<>(subTaskList.values());
    }

    @Override
    public ArrayList<Task> getAllEpic() {
        return new ArrayList<>(epicList.values());
    }

    @Override
    public void deleteAllTask() {
        if (!taskList.isEmpty()) {
            for (Task task : taskList.values()) {
                historyManager.remove(task.getID());
            }
            taskList.clear();
        }
    }

    @Override
    public void deleteAllEpic() {
        if (!epicList.isEmpty()) {
            for (Task task : epicList.values()) {
                historyManager.remove(task.getID());
            }
            epicList.clear();
            deleteAllSubTask();
        }
    }

    @Override
    public void deleteAllSubTask() {
        if (!subTaskList.isEmpty()) {
            ArrayList<SubTask> deleteList = new ArrayList<>(subTaskList.values());
            for (SubTask subTask : deleteList) {
                removeTask(subTask);
                historyManager.remove(subTask.getID());
            }
        }
    }

    @Override
    public void removeTask(Task task) {
        int taskID = getTaskID(task);
        if (taskList.containsKey(taskID)) {
            taskList.remove(taskID);
            historyManager.remove(taskID);
        } else if (epicList.containsKey(taskID)) {
            if (!subTaskList.isEmpty()) {
                ArrayList<SubTask> deleteList = new ArrayList<>(subTaskList.values());
                for (SubTask subTask : deleteList) {
                    if (subTask.getEpicID() == taskID) {
                        removeTask(subTask);
                        historyManager.remove(subTask.getID());
                    }
                }
            }
            epicList.remove(taskID);
            historyManager.remove(taskID);
        } else if (subTaskList.containsKey(taskID)) {
            Epic epic = epicList.get(subTaskList.get(taskID).getEpicID());
            epic.removeSubTaskFromEpic(subTaskList.get(taskID));
            subTaskList.remove(taskID);
            historyManager.remove(taskID);
        }
    }

    @Override
    public void updateTask(int taskID, Task newTask) {
        newTask.setID(taskID);
        taskList.replace(taskID,newTask);
    }

    @Override
    public void updateEpic(int epicID, Epic newEpic) {
        newEpic.setID(epicID);
        newEpic.updateStatus();
        for (SubTask subTask : subTaskList.values()) {
            if (subTask.getEpicID() == epicID) {
                newEpic.addSubTaskToEpic(subTask);
            }
        }
        epicList.replace(epicID,newEpic);
    }

    @Override
    public void updateSubTask(int taskID, SubTask newSubTask) {
        newSubTask.setID(taskID);
        Epic epic = epicList.get(newSubTask.getEpicID());
        epic.removeSubTaskFromEpic(subTaskList.get(taskID));
        subTaskList.replace(taskID,newSubTask);
        epic.addSubTaskToEpic(newSubTask);
    }

    @Override
    public void setStatusTask(Task task, Status newStatus) {
        task.setStatus(newStatus);
    }

    @Override
    public void setStatusSubTask(SubTask subTask, Status newStatus) {
        subTask.setStatus(newStatus);
        epicList.get(subTask.getEpicID()).updateStatus();
    }

    @Override
    public ArrayList<Task> getSubTasksFromEpic(Epic epic) {
        return epic.getListSubTasks();
    }

    @Override
    public int getTaskID(Task task) {
        return task.getID();
    }

}
