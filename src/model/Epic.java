package model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<SubTask> listSubTasks;
    String status;

    public Epic(String taskName, String description, int id) {
        super.name = taskName;
        super.description = description;
        super.id = id;
        listSubTasks = new ArrayList<>();
    }

    public void addSubTaskToEpic(SubTask subTask){
        this.listSubTasks.add(subTask);
        updateStatus();
    }

    public void removeSubTaskFromEpic(SubTask subTask){
        listSubTasks.remove(subTask);
        updateStatus();
    }

    public ArrayList getListSubTasks(){
        return (ArrayList<SubTask>) listSubTasks.clone();
    }

    public void updateStatus(){
        String newStatus = null;
        int newSum = 0;
        int doneSum = 0;
        if (listSubTasks.isEmpty()){
            newStatus = "NEW";
        } else {
            for (Task subTask : listSubTasks) {
                if (subTask.status.equals("IN_PROGRESS")) {
                    newStatus = "IN_PROGRESS";
                    break;
                } else {
                    if (subTask.status.equals("NEW")) {
                        newSum++;
                    } else if (subTask.status.equals("DONE")) {
                        doneSum++;
                    }
                    if (newSum == listSubTasks.size()) {
                        newStatus = "NEW";
                    } else if (doneSum == listSubTasks.size()) {
                        newStatus = "DONE";
                    }
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
