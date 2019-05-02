package track_ninja.playlist_generator.exceptions;

public class DurationToShortException extends IllegalArgumentException {
    private static final String MESSAGE = "Travel duration too short!";

    @Override
    public String getMessage() {
        return MESSAGE;
    }
}
