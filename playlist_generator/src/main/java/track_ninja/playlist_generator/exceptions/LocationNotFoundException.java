package track_ninja.playlist_generator.exceptions;

public class LocationNotFoundException extends IllegalArgumentException {
    public LocationNotFoundException(String s) {
        super(s);
    }
}
