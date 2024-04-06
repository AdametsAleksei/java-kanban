package model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Task> listSubTasks;

    public Epic(String taskName, String description) {
        super(taskName, description, Status.NEW);
        listSubTasks = new ArrayList<>();
    }

    public void addSubTaskToEpic(SubTask subTask) {
        this.listSubTasks.add(subTask);
        updateStatus();
    }

    public void removeSubTaskFromEpic(SubTask subTask) {
        listSubTasks.remove(subTask);
        updateStatus();
    }

    public ArrayList<Task> getListSubTasks() {
        return (ArrayList<Task>) listSubTasks.clone();
    }

    public void updateStatus() {
        int newSum = 0;
        int doneSum = 0;
        if (listSubTasks.isEmpty()) {
            this.status = Status.NEW;
        } else {
            for (Task subTask : listSubTasks) {
                if (subTask.status.equals(Status.IN_PROGRESS)) {
                    this.status = Status.IN_PROGRESS;
                    break;
                } else {
                    if (subTask.status.equals(Status.NEW)) {
                        newSum++;
                    } else if (subTask.status.equals(Status.DONE)) {
                        doneSum++;
                    }
                    if (newSum == listSubTasks.size()) {
                        this.status = Status.NEW;
                        break;
                    } else if (doneSum == listSubTasks.size()) {
                        this.status = Status.DONE;
                        break;
                    } else {
                        this.status = Status.IN_PROGRESS;
                    }
                }
            }
        }
    }

    @Override
    public String toFile() {
        return id + "," + Type.Epic + "," + name + "," + status + "," + description;
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
