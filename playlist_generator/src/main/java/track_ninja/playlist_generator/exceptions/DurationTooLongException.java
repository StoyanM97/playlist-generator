package track_ninja.playlist_generator.exceptions;

public class DurationTooLongException extends IllegalArgumentException {

    private static final String MESSAGE = "Travel duration too long!";

    @Override
    public String getMessage() {
        return MESSAGE;
    }
}
