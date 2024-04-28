package controllers;

import exceptions.TimeReservedException;
import model.*;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> taskList = new HashMap<>();
    private final HashMap<Integer, Epic> epicList = new HashMap<>();
    private final HashMap<Integer, SubTask> subTaskList = new HashMap<>();
    private int id = 1;
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private final Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    private boolean validate(Task taskForValidate) {
        return getPrioritizedTasks().stream()
                .filter(task -> !task.getClass().equals(Epic.class) && task.getStartTime().isBefore(taskForValidate.getEndTime())
                        & task.getEndTime().isAfter(taskForValidate.getStartTime()))
                .toList().isEmpty();
    }

    protected void addToHistory(Task task) {
        historyManager.addToHistory(task);
    }

    private void addToSetPrioritizedTasks(Task task) {
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    private void removeFromSetPrioritizedTasks(Task task) {
        prioritizedTasks.remove(task);
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return List.copyOf(prioritizedTasks);
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
        while (true) {
            if (taskList.containsKey(id) || epicList.containsKey(id) || subTaskList.containsKey(id)) {
                id++;
            } else {
                break;
            }
        }
        return id;
    }

    @Override
    public void createTask(Task task) {
        if (validate(task)) {
            if (task.getID().getAsInt() == 0) {
                task.setID(makeIDTask());
            }
            taskList.put(task.getID().getAsInt(), task);
            addToSetPrioritizedTasks(task);
        } else {
            throw new TimeReservedException("Время занято для - " + task.getName());
        }
    }

    @Override
    public void createEpic(Epic epic) {
        if (epic.getID().getAsInt() == 0) {
            epic.setID(makeIDTask());
        }
        epicList.put(epic.getID().getAsInt(),epic);
    }

    @Override
    public void createSubTask(SubTask subTask) {
        if (validate(subTask)) {
            if (subTask.getID().getAsInt() == 0) {
                subTask.setID(makeIDTask());
            }
            if (epicList.containsKey(subTask.getEpicID())) {
                subTaskList.put(subTask.getID().getAsInt(), subTask);
                epicList.get(subTask.getEpicID()).addSubTaskToEpic(subTask);
                addToSetPrioritizedTasks(subTask);
            }
        } else {
            throw new TimeReservedException("Время занято для - " + subTask.getName());
        }
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
                historyManager.remove(task.getID().getAsInt());
                removeFromSetPrioritizedTasks(task);
            }
            taskList.clear();
        }
    }

    @Override
    public void deleteAllEpic() {
        if (!epicList.isEmpty()) {
            deleteAllSubTask();
            for (Task task : epicList.values()) {
                if (task.getID().getAsInt() != 0) {
                    historyManager.remove(task.getID().getAsInt());
                }
            }
            epicList.clear();
        }
    }

    @Override
    public void deleteAllSubTask() {
        if (!subTaskList.isEmpty()) {
            ArrayList<SubTask> deleteList = new ArrayList<>(subTaskList.values());
            for (SubTask subTask : deleteList) {
                removeTask(subTask);
                if (subTask.getID().isPresent()) {
                    historyManager.remove(subTask.getID().getAsInt());
                }
                removeFromSetPrioritizedTasks(subTask);
            }
        }
    }

    @Override
    public void removeTask(Task task) {
        int taskID = getTaskID(task);
        if (taskList.containsKey(taskID)) {
            taskList.remove(taskID);
            historyManager.remove(taskID);
            removeFromSetPrioritizedTasks(task);
        } else if (epicList.containsKey(taskID)) {
            if (!subTaskList.isEmpty()) {
                ArrayList<SubTask> deleteList = new ArrayList<>(subTaskList.values());
                for (SubTask subTask : deleteList) {
                    if (subTask.getEpicID() == taskID) {
                        removeTask(subTask);
                        historyManager.remove(subTask.getID().getAsInt());
                        removeFromSetPrioritizedTasks(subTask);
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
            removeFromSetPrioritizedTasks(task);
        }
    }

    @Override
    public void updateTask(int taskID, Task newTask) {
        removeFromSetPrioritizedTasks(getTaskFromID(taskID));
        if (validate(newTask)) {
            newTask.setID(taskID);
            taskList.replace(taskID, newTask);
            addToSetPrioritizedTasks(newTask);
        } else {
            throw new TimeReservedException("Время занято для - " + newTask.getName());
        }
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
        removeFromSetPrioritizedTasks(getTaskFromID(taskID));
        if (validate(newSubTask)) {
            newSubTask.setID(taskID);
            Epic epic = epicList.get(newSubTask.getEpicID());
            epic.removeSubTaskFromEpic(subTaskList.get(taskID));
            subTaskList.replace(taskID, newSubTask);
            epic.addSubTaskToEpic(newSubTask);
        } else {
            throw new TimeReservedException("Время занято для - " + newSubTask.getName());
        }
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
        return task.getID().getAsInt();
    }

}
