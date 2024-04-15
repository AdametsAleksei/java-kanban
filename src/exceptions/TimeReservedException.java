package exceptions;

public class TimeReservedException extends RuntimeException {

    public TimeReservedException(String message) {
        super(message);
    }
}
