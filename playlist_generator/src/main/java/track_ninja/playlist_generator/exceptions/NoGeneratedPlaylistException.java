package track_ninja.playlist_generator.exceptions;

public class NoGeneratedPlaylistException extends IllegalArgumentException {
    private static final String NO_PLAYLIST_WITH_THIS_ID_ERROR_MESSAGE = "No playlist with this id!";


    @Override
    public String getMessage() {
        return NO_PLAYLIST_WITH_THIS_ID_ERROR_MESSAGE;
    }
}
