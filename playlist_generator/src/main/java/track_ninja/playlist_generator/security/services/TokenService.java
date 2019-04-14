package track_ninja.playlist_generator.security.services;

public interface TokenService<T> {
    String getUsernameFromToken(T obj);
}
