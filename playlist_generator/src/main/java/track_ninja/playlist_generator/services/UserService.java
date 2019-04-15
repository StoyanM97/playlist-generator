package track_ninja.playlist_generator.services;

import track_ninja.playlist_generator.models.User;

import java.util.List;

public interface UserService {
    User getByUsername(String usernameFromToken);

    Iterable<User> getAll();
}
