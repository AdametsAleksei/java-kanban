import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task{
    private ArrayList<SubTask> listSubTasks;

    public Epic(String taskName, String description, int id, String status) {
        super(taskName, description, id, status);
        listSubTasks = new ArrayList<>();
    }

    public void addSubTaskToEpic(SubTask subTask){
        this.listSubTasks.add(subTask);
        updateStatus(this);
    }

    public void removeSubTaskFromEpic(SubTask subTask){
        listSubTasks.remove(subTask);
        updateStatus(this);
    }

    public ArrayList getListSubTasks(){
        return (ArrayList<SubTask>) listSubTasks.clone();
    }
    public void updateStatus(Epic epic){
        String newStatus = null;
        int newSum = 0;
        int doneSum = 0;
        if (listSubTasks.isEmpty()){
            newStatus = "NEW";
        } else {
            for (Task subTask : listSubTasks) {
                if (Objects.equals(subTask.status, "NEW")) {
                    newSum++;
                } else if (Objects.equals(subTask.status, "DONE")) {
                    doneSum++;
                }
                if (newSum == listSubTasks.size()) {
                    newStatus = "NEW";
                } else if (doneSum == listSubTasks.size()) {
                    newStatus = "DONE";
                } else {
                    newStatus = "IN_PROGRESS";
                }
            }
        }
        this.status = newStatus;
    }


    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                " listSubTasks.size()=" + listSubTasks.size() +
                ", id=" + id +
                '}';
    }

}
