package model;

public class SubTask extends Task {
    private final int epicID;

    public SubTask(String taskName, String description, Status status, int epicID) {
        super(taskName, description, status);
        this.epicID = epicID;
    }

    public int getEpicID() {
        return this.epicID;
    }

    @Override
    public String toFile() {
        return id + "," + Type.SubTask + "," + name + "," + status + "," + description + "," + epicID;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicID=" + epicID +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", id=" + id +
                '}';
    }
}
