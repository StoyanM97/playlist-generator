package track_ninja.playlist_generator.models.exceptions;

public class UserNotFoundException extends IllegalArgumentException {

    private static final String MESSAGE = "No users found with this username!";

    @Override
    public String getMessage() {
        return MESSAGE;
    }
}
