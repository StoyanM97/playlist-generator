package track_ninja.playlist_generator.services;

import track_ninja.playlist_generator.models.User;
import track_ninja.playlist_generator.security.models.LoginUser;

import java.util.List;

public interface UserService {
    User getByUsername(String username);

    Iterable<User> getAll();

    void register(LoginUser loginUser);

    void delete(String username);
}
