package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.OptionalInt;



public class Task {
    protected final String name;
    protected final String description;
    protected int id;
    protected Status status;
    protected Duration duration;
    protected LocalDateTime startTime;

    public Task(String name, String description, Status status, Duration duration, String startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        if (startTime != null) {
            this.startTime = LocalDateTime.parse(startTime, TimeFormatter.formatter);
        }
    }

    public LocalDateTime getEndTime() {
        return this.startTime.plus(duration);
    }

    public void setStartTime(String startTime) {
        if (startTime == null) {
            this.startTime = null;
        } else {
            this.startTime = LocalDateTime.parse(startTime, TimeFormatter.formatter);
        }
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setStatus(Status newStatus) {
        this.status = newStatus;
    }

    public OptionalInt getID() {
        return OptionalInt.of(this.id);
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public Status getStatus() {
        return this.status;
    }

    public String toFile() {
        return id + "," + Type.Task + "," + name + "," + status
                + "," + description + "," + duration.toMinutes() + "," + startTime.format(TimeFormatter.formatter);
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (id != 0) {
            return hash + (id * 31);
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        return this.id == ((Task) obj).id;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", id=" + id +
                ", startTime=" + startTime +
                ", Duration=" + duration +
                '}';
    }
}
