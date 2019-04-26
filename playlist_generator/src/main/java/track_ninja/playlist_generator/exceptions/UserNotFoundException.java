package track_ninja.playlist_generator.exceptions;

public class UserNotFoundException extends IllegalArgumentException {

    private static final String MESSAGE = "No user found with this username!";


    @Override
    public String getMessage() {
        return MESSAGE;
    }
}
