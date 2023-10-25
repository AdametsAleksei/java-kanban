import java.util.ArrayList;

public class TaskManager {
    ArrayList<Task> tasksList = new ArrayList<>();
    int id = 0;

    private int makeIDTask(){
        return id +=1;
    }

    public Task createTask(String name, String description, String status){
        Task task = new Task(name, description, makeIDTask(), status);
        tasksList.add(task);
        return task;
    }
    public ArrayList getTasksList(){
        return new ArrayList<>(tasksList);
    }

    public int getTaskID(Task task){
        return task.getID(task);
    }
}
