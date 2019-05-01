package track_ninja.playlist_generator.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import track_ninja.playlist_generator.models.User;
import track_ninja.playlist_generator.models.dtos.UserEditDTO;
import track_ninja.playlist_generator.models.dtos.UserRegistrationDTO;
import track_ninja.playlist_generator.models.dtos.UserDisplayDTO;
import track_ninja.playlist_generator.security.models.LoginUser;

import java.io.IOException;
import java.util.List;


public interface UserService {
    User getByUsername(String username);

    UserDisplayDTO getUser(String username);

    ResponseEntity getLoggedUser(LoginUser loginUser);

    List<UserDisplayDTO> getAll();

    boolean register(UserRegistrationDTO registrationUser);

    boolean createUser(UserRegistrationDTO userRegistrationDTO);

    boolean editUserByAdmin(UserEditDTO userEditDTO);

    boolean deleteUser(String username);

    boolean editUser(UserEditDTO userEditDTO);

    boolean avatarUpload(MultipartFile file, String username) throws IOException;

    List<UserDisplayDTO> findAllByUsernameLike(String username);

}
