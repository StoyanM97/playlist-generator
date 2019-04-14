package track_ninja.playlist_generator.services;

import track_ninja.playlist_generator.models.User;

public interface UserService {
    User getByUsername(String usernameFromToken);
}
