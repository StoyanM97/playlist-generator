package track_ninja.playlist_generator.services;

import org.springframework.http.ResponseEntity;
import track_ninja.playlist_generator.models.User;
import track_ninja.playlist_generator.models.dtos.RegistrationDTO;
import track_ninja.playlist_generator.security.models.LoginUser;


public interface UserService {
    User getByUsername(String username);

    ResponseEntity getLoggedUser(LoginUser loginUser);

    Iterable<User> getAll();

    boolean register(RegistrationDTO registrationUser);

    void delete(String username);
}
