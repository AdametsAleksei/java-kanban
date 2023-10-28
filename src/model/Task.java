package model;

public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected String status;

    public Task(String name, String description, int id, String status) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
    }
    public Task(){}

    public void setStatus(String newStatus){
        this.status = newStatus;
    }

    public int getID(){
        return this.id;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", id=" + id +
                '}';
    }
}
