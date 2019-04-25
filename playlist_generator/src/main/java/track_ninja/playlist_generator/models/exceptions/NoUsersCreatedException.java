package track_ninja.playlist_generator.models.exceptions;

public class NoUsersCreatedException extends IllegalArgumentException {

    private static final String MESSAGE = "No users created!";

    @Override
    public String getMessage() {
        return MESSAGE;
    }
}
