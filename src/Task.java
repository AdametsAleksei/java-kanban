public class Task {
    private String taskName;
    private String description;
    private int id;
    private String status;

    public Task(String taskName, String description, int id, String status) {
        this.taskName = taskName;
        this.description = description;
        this.id = id;
        this.status = status;
    }

    public int getID(Task task){
        return task.id;
    }
}
