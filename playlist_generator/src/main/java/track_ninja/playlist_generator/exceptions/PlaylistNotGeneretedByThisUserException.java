package track_ninja.playlist_generator.exceptions;

public class PlaylistNotGeneretedByThisUserException extends IllegalArgumentException {


    private static final String PLAYLIST_NOT_GENERATED_BY_THIS_USER_ERROR_MESSAGE = "Playlist not generated by this user!";
//
    @Override
    public String getMessage() {
        return PLAYLIST_NOT_GENERATED_BY_THIS_USER_ERROR_MESSAGE;
    }
}
