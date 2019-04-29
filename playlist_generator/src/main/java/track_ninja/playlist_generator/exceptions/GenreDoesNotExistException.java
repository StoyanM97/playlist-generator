package track_ninja.playlist_generator.exceptions;

public class GenreDoesNotExistException extends IllegalArgumentException {

    private static final String MESSAGE = "No genre with this name exists!";

    @Override
    public String getMessage() {
        return MESSAGE;
    }
}
