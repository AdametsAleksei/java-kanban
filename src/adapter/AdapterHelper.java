package adapter;

import model.Status;

import java.time.format.DateTimeFormatter;

public final class AdapterHelper {
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy - HH:mm");

    public static String statusToString(Status status) {
        switch (status) {
            case NEW -> {
                return "NEW";
            }
            case IN_PROGRESS -> {
                return "IN_PROGRESS";
            }
            case DONE -> {
                return "DONE";
            }
            default -> {
                return "UNKNOWN";
            }
        }
    }

    public static Status statusFromString(String status) {
        switch (status) {
            case "NEW" -> {
                return Status.NEW;
            }
            case "IN_PROGRESS" -> {
                return Status.IN_PROGRESS;
            } case "DONE" -> {
                return Status.DONE;
            }
            default -> {
                return null;
            }
        }
    }
}
