package track_ninja.playlist_generator.models.exceptions;

public class NoGeneratedPlaylistsException extends IllegalArgumentException {
    public NoGeneratedPlaylistsException(String s) {
        super(s);
    }
}
