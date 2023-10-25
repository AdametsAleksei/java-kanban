public class SubTask extends Task{
    private int epicID;
    public SubTask(String taskName, String description, int id, String status, int epicID) {
        super(taskName, description, id, status);
        this.epicID = epicID;
    }

    public int getEpicID(){
        return this.epicID;
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