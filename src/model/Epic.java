package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    public final ArrayList<Task> listSubTasks;
    private LocalDateTime endTime;

    public Epic(String taskName, String description) {
        super(taskName, description, Status.NEW, null, null);
        listSubTasks = new ArrayList<>();
    }

    public void updateTimeEpic() {
        Duration duration = null;
        if (listSubTasks.isEmpty()) {
            setDuration(null);
            setStartTime(null);
        } else {
            for (Task task : listSubTasks) {
                if (getDuration() == null) {
                    setDuration(task.getDuration());
                    duration = task.getDuration();
                } else {
                    if (duration == null) {
                        duration = task.getDuration();
                    } else {
                        duration = duration.plus(task.getDuration());
                        setDuration(duration);
                    }
                }
                if (getStartTime() == null || task.getStartTime().isBefore(getStartTime())) {
                    setStartTime(task.getStartTime().format(formatter));
                }
                if (getEndTime() == null || task.getEndTime().isAfter(getEndTime())) {
                    endTime = task.getEndTime();
                }
            }
        }
    }

    public void addSubTaskToEpic(SubTask subTask) {
        this.listSubTasks.add(subTask);
        updateTimeEpic();
        updateStatus();
    }

    public void removeSubTaskFromEpic(SubTask subTask) {
        listSubTasks.remove(subTask);
        updateTimeEpic();
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
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toFile() {
        return id + "," + Type.Epic + "," + name + ","
                + status + "," + description; // + "," + duration.toMinutes() + "," + startTime;
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
