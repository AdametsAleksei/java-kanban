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
        for (Task task: epicList.values()){
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
                epic.removeSubTaskFromEpic(epic, subTaskList.get(taskID));
                subTaskList.remove(taskID);
        }
    }

    public void updateTask(Task oldTask, Task newTask){
        if (oldTask.getClass().equals(newTask.getClass())){
            if (oldTask.getClass().toString().equals("class Task")){

            }
        }
    }

    public int getTaskID(Task task){
        return task.getID(task);
    }

}
