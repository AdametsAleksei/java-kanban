import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasksList = new HashMap<>();
    private HashMap<Integer, Epic> epicList = new HashMap<>();
    private HashMap<Integer, SubTask> subTaskList = new HashMap<>();
    private int id = 0;

    private int makeIDTask(){
        return id +=1;
    }

    public Task createTask(String name, String description, String status){
        Task task = new Task(name, description, makeIDTask(), status);
        tasksList.put(task.getID(task),task);
        return task;
    }

    public Epic createEpic(String name, String description){
        Epic epic = new Epic(name, description, makeIDTask(), "NEW");
        epicList.put(epic.getID(epic),epic);
        return epic;
    }

    public SubTask createSubTask(String name, String description, String status, Epic epic){
        SubTask subTask = new SubTask(name, description, makeIDTask(), status, epic.getID(epic));
        subTaskList.put(subTask.getID(subTask),subTask);
        epic.addSubTaskToEpic(subTask);
        return subTask;
    }

    public ArrayList getAllTask(){
        ArrayList<Task> list = new ArrayList<>();
        for (Task task: tasksList.values()){
            list.add(task);
        }
        return list;
    }

    public ArrayList getAllSubTask(){
        ArrayList<Task> list = new ArrayList<>();
        for (Task task: subTaskList.values()){
            list.add(task);
        }
        return list;
    }

    public ArrayList getAllEpic(){
        ArrayList<Task> list = new ArrayList<>();
        for (Task task: epicList.values()){
            list.add(task);
        }
        return list;
    }

    public void deleteAllTask(){
        tasksList.clear();
    }

    public void deleteAllEpic(){
        epicList.clear();
    }

    public void deleteAllSubTask(){
            for (Task subTask : subTaskList.values()) {
                if (subTaskList != null) {
                removeTask(subTask);
            }
        }
        subTaskList.clear();
    }

    public Task getTaskFromID(Task task){
        int taskID = task.getID(task);
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
        Epic newEpic = new Epic(name, description, epicID, "NEW");
        newEpic.updateStatus(newEpic);
        for(SubTask subTask : subTaskList.values()){
            if (subTask.getEpicID() == epicID){
                newEpic.addSubTaskToEpic(subTask);
            }
        }
        epicList.replace(epicID,newEpic);
    }

    public void updateSubTask(int taskID, Epic epic, String name, String description, String status){
        SubTask subTask = new SubTask(name, description, taskID, status, epic.getID(epic));
        epic.removeSubTaskFromEpic(subTaskList.get(taskID));
        subTaskList.replace(taskID,subTask);
        epic.addSubTaskToEpic(subTask);
    }

    public void setStatusTask(Task task, String newStatus){
        task.setStatus(newStatus);
    }

    public void setStatusSubTask(SubTask subTask, String newStatus){
        subTask.setStatus(newStatus);
        epicList.get(subTask.getEpicID()).updateStatus(epicList.get(subTask.getEpicID()));
    }

    public ArrayList getSubTasksFromEpic(Epic epic){
        return epic.getListSubTasks();
    }

    public int getTaskID(Task task){
        return task.getID(task);
    }

}
