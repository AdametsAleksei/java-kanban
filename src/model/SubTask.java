package model;

import java.time.Duration;

public class SubTask extends Task {
    private final int epicID;

    public SubTask(String taskName, String description, Status status,
                   int epicID, Duration duration, String startTime) {
        super(taskName, description, status, duration, startTime);
        this.epicID = epicID;
    }

    public int getEpicID() {
        return this.epicID;
    }

    @Override
    public String toFile() {
        return id + "," + Type.SubTask + "," + name + "," + status + ","
                + description + "," + epicID + "," + duration.toMinutes()
                + "," + startTime.format(TimeFormatter.formatter);
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
