package track_ninja.playlist_generator.exceptions;

public class UsernameAlreadyExistsException extends IllegalArgumentException {

    private static final String MESSAGE = "A user with this username already exists!";

    @Override
    public String getMessage() {
        return MESSAGE;
    }
}
