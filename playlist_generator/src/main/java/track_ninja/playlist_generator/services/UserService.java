package track_ninja.playlist_generator.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import track_ninja.playlist_generator.models.User;
import track_ninja.playlist_generator.models.dtos.CreateEditUserByAdminDTO;
import track_ninja.playlist_generator.models.dtos.RegistrationDTO;
import track_ninja.playlist_generator.models.dtos.UserDTO;
import track_ninja.playlist_generator.security.models.LoginUser;

import java.util.List;


public interface UserService {

    User getByUsername(String username);

    UserDTO getUser(String username);

    ResponseEntity getLoggedUser(LoginUser loginUser);

    List<UserDTO> getAll();

    boolean register(RegistrationDTO registrationUser);

    boolean createUser(CreateEditUserByAdminDTO createEditUserByAdminDTO);

    boolean editUserByAdmin(CreateEditUserByAdminDTO createEditUserByAdminDTO);

    boolean deleteUser(String username);

    boolean editUser(RegistrationDTO registrationDTO);

    boolean avatarUpload(MultipartFile file, String username);

}
