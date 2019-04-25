package track_ninja.playlist_generator.services;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import track_ninja.playlist_generator.models.User;
import track_ninja.playlist_generator.models.UserDetailsModel;
import track_ninja.playlist_generator.models.dtos.*;
import track_ninja.playlist_generator.models.exceptions.NoUsersCreatedException;
import track_ninja.playlist_generator.models.exceptions.UserNotFoundException;
import track_ninja.playlist_generator.models.exceptions.UsernameAlreadyExistsException;
import track_ninja.playlist_generator.models.mappers.ModelMapper;
import track_ninja.playlist_generator.repositories.AuthorityRepository;
import track_ninja.playlist_generator.repositories.UserDetailsRepository;
import track_ninja.playlist_generator.repositories.UserRepository;
import track_ninja.playlist_generator.security.models.JwtTokenUtil;
import track_ninja.playlist_generator.security.models.LoginUser;

import java.util.ArrayList;
import java.util.List;

@Service("UserServiceImpl")
public class UserServiceImpl implements UserService, UserDetailsService {

    private UserRepository userRepository;
    private AuthorityRepository authorityRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenUtil jwtTokenUtil;
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                           AuthorityRepository authorityRepository, JwtTokenUtil jwtTokenUtil,
                           UserDetailsRepository userDetailsRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsRepository = userDetailsRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsernameAndEnabledTrue(username);
    }

    @Override
    public List<UserDisplayDTO> getAll() {
        List<UserDisplayDTO> userDisplayDTOS = new ArrayList<>();
        Iterable<User> users = userRepository.findAll();
        if (!users.iterator().hasNext()) {
            throw new NoUsersCreatedException();
        }
        users.forEach(user -> userDisplayDTOS.add(ModelMapper.userToDTO(user)));
        return userDisplayDTOS;
    }

    @Override
    public UserDisplayDTO getUser(String username) {
        return ModelMapper.userToDTO(getByUsername(username));
    }

    @Override
    public User getByUsername(String username) {
        User user = userRepository.findByUsernameAndEnabledTrue(username);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }

    @Override
    public ResponseEntity getLoggedUser(LoginUser loginUser){

        final User user = userRepository.findByUsernameAndEnabledTrue((loginUser.getUsername()));
        final String token = jwtTokenUtil.generateToken(user);
        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setUsername(user.getUsername());
        loginDTO.setRole(user.getAuthority().getName().toString());
        loginDTO.setFirstName(user.getUserDetail().getFirstName());
        loginDTO.setLastName(user.getUserDetail().getLastName());
        loginDTO.setEmail(user.getUserDetail().getEmail());
        loginDTO.setAvatar(getAvatar(user));
        loginDTO.setToken(token);

        return ResponseEntity.ok(loginDTO);
    }

    @Override
    public boolean register(UserRegistrationDTO registrationUser) {
        if (userRepository.existsByUsername(registrationUser.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }
        User user = new User();
        mapRegistrationDTOToUser(registrationUser, user);
        UserDetailsModel userDetailsModel = new UserDetailsModel();
        mapRegistrationDTOToUserDetails(registrationUser, user, userDetailsModel);
        return userRepository.save(user) != null;
    }

    @Override
    public boolean deleteUser(String username) {
        System.out.println(username);
        User user = userRepository.findByUsernameAndEnabledTrue(username);
        if (user == null) {
            throw new UserNotFoundException();
        }
        user.setEnabled(false);

        return userRepository.save(user) != null;

    }

    @Override
    public boolean createUser(UserRegistrationDTO userRegistrationDTO) {
        if (userRepository.existsByUsername(userRegistrationDTO.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }
        UserDetailsModel userDetails = new UserDetailsModel();
        userDetails.setFirstName(userRegistrationDTO.getFirstName());
        userDetails.setLastName(userRegistrationDTO.getLastName());
        userDetails.setEmail(userRegistrationDTO.getEmail());
        userDetails.setDeleted(false);
        mapCreateEditAdminDTOtoUserDetails(userRegistrationDTO, userDetails);
        User user = new User();
        user.setUsername(userRegistrationDTO.getUsername());
        user.setPassword(userRegistrationDTO.getPassword());
        user.setUserDetail(userDetails);
        user.setAuthority(authorityRepository.findByName(userRegistrationDTO.getRole()));
        user.setEnabled(true);

        mapCreateEditUserByAdminDTOtoUser(userRegistrationDTO, userDetails, user);
        return userRepository.save(user) != null;
    }

    @Override
    public boolean editUserByAdmin(UserEditDTO userEditDTO) {
        User user = getByUsername(userEditDTO.getUsername());
        if (user == null) {
            throw new UserNotFoundException();
        }
        UserDetailsModel userDetails = userDetailsRepository.findByIsDeletedFalseAndUser_Username(userEditDTO.getUsername());
        mapCreateEditAdminDTOtoUserDetails(userEditDTO, userDetails);
        mapCreateEditUserByAdminDTOtoUser(userEditDTO, userDetails, user);
        return userRepository.save(user) != null;
    }

    @Override
    public boolean editUser(UserEditDTO userEditDTO) {

        User user = userRepository.findByUsernameAndEnabledTrue(userEditDTO.getUsername());
        if (user == null) {
            throw new UserNotFoundException();
        }
        mapRegistrationDTOToUser(userEditDTO, user);
        UserDetailsModel userDetailsModel =userDetailsRepository.findByIsDeletedFalseAndUser_Username(userEditDTO.getUsername());
        mapRegistrationDTOToUserDetails(userEditDTO, user, userDetailsModel);

        return false;
    }

    @Override
    public boolean avatarUpload(MultipartFile file, String username) {
        return false;
    }

    private String getAvatar(User user){
        return user.getUserDetail().getAvatar()==null? null : new String(Base64.encodeBase64(user.getUserDetail().getAvatar()));
    }

    private void mapRegistrationDTOToUserDetails(UserRegistrationDTO registrationUser, User user, UserDetailsModel userDetailsModel) {
        userDetailsModel.setEmail(registrationUser.getEmail());
        userDetailsModel.setFirstName(registrationUser.getFirstName());
        userDetailsModel.setLastName(registrationUser.getLastName());
        userDetailsModel.setUser(user);
        user.setUserDetail(userDetailsModel);
    }

    private void mapRegistrationDTOToUser(UserRegistrationDTO registrationUser, User user) {
        user.setUsername(registrationUser.getUsername());
        user.setPassword(passwordEncoder.encode(registrationUser.getPassword()));
        user.setAuthority(authorityRepository.findById(1).orElse(null));
        user.setEnabled(true);
    }

    private void mapCreateEditUserByAdminDTOtoUser(UserRegistrationDTO userRegistrationDTO, UserDetailsModel userDetails, User user) {
        user.setUsername(userRegistrationDTO.getUsername());
        user.setPassword(userRegistrationDTO.getPassword());
        user.setUserDetail(userDetails);
        user.setAuthority(authorityRepository.findByName(userRegistrationDTO.getRole()));
        user.setEnabled(true);
    }

    private void mapCreateEditAdminDTOtoUserDetails(UserRegistrationDTO userRegistrationDTO, UserDetailsModel userDetails) {
        userDetails.setFirstName(userRegistrationDTO.getFirstName());
        userDetails.setLastName(userRegistrationDTO.getLastName());
        userDetails.setEmail(userRegistrationDTO.getEmail());
        userDetails.setDeleted(false);
    }
}
