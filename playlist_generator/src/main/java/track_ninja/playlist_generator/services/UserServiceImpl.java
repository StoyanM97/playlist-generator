package track_ninja.playlist_generator.services;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import track_ninja.playlist_generator.models.User;
import track_ninja.playlist_generator.models.UserDetails;
import track_ninja.playlist_generator.models.dtos.*;
import track_ninja.playlist_generator.exceptions.UserNotFoundException;
import track_ninja.playlist_generator.exceptions.UsernameAlreadyExistsException;
import track_ninja.playlist_generator.models.mappers.ModelMapper;
import track_ninja.playlist_generator.repositories.AuthorityRepository;
import track_ninja.playlist_generator.repositories.UserDetailsRepository;
import track_ninja.playlist_generator.repositories.UserRepository;
import track_ninja.playlist_generator.security.models.JwtTokenUtil;
import track_ninja.playlist_generator.security.models.LoginUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("UserServiceImpl")
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final String RETRIEVING_ALL_USERS_MESSAGE = "Retrieving all users...";
    private static final String RETRIEVED_USERS_MESSAGE = "Retrieved users!";
    private static final String RETRIEVING_USER_BY_USERNAME_MESSAGE = "Retrieving user by username...";
    private static final String RETRIEVED_USER_MESSAGE = "Retrieved user!";
    private static final String LOGGING_USER_IN_MESSAGE = "Logging user %s in...";
    private static final String COULD_NOT_LOG_USER_IN_ERROR_MESSAGE = "Could not log user in! %s";
    private static final String LOGIN_SUCCESSFUL_MESSAGE = "Login successful!";
    private static final String REGISTERING_USER_MESSAGE = "Registering user %s...";
    private static final String REGISTRATION_SUCCESSFUL_MESSAGE = "Registration successful!";
    private static final String DELETING_USER_MESSAGE = "Deleting user %s...";
    private static final String DELETION_SUCCESSFUL_MESSAGE = "Deletion successful!";
    private static final String CREATING_USER_MESSAGE = "Creating user %s...";
    private static final String CREATION_SUCCESSFUL_MESSAGE = "Creation successful!";
    private static final String EDITING_USER_MESSAGE = "Editing user %s...";
    private static final String USER_SUCCESSFULLY_EDITED_MESSAGE = "User successfully edited!";

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private static final String UPLOADING_AVATAR_MESSAGE = "Uploading avatar...";
    private static final String AVATAR_UPLOADED_MESSAGE = "Avatar uploaded!";
    private static final String LOOKING_FOR_USERS_MESSAGE = "Looking for user with username like %s";
    private static final String USERS_FOUND_MESSAGE = "User/s found!";
    private static final String MATCHER = "%";

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
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsernameAndEnabledTrue(username);
    }

    @Override
    public List<UserDisplayDTO> getAll() {
        logger.info(RETRIEVING_ALL_USERS_MESSAGE);
        List<UserDisplayDTO> userDisplayDTOS = new ArrayList<>();
        List<User> users = userRepository.findAllByEnabledTrue();
        users.forEach(user -> {
            if (!user.getUserDetail().isDeleted()) {
                userDisplayDTOS.add(ModelMapper.userToDTO(user));
            }
        });
        logger.info(RETRIEVED_USERS_MESSAGE);
        return userDisplayDTOS;
    }

    @Override
    public UserDisplayDTO getUser(String username) {
        return ModelMapper.userToDTO(getByUsername(username));
    }

    @Override
    public User getByUsername(String username) {
        logger.info(RETRIEVING_USER_BY_USERNAME_MESSAGE);
        User user = userRepository.findByUsernameAndEnabledTrue(username);
        if (user == null || !user.isEnabled() || user.getUserDetail().isDeleted()) {
            throw new UserNotFoundException();
        }
        logger.info(RETRIEVED_USER_MESSAGE);
        return user;
    }

    @Override
    public ResponseEntity getLoggedUser(LoginUser loginUser) {
        logger.info(String.format(LOGGING_USER_IN_MESSAGE, loginUser.getUsername()));
        final User user = userRepository.findByUsernameAndEnabledTrue((loginUser.getUsername()));
        if (user == null || !user.isEnabled() || user.getUserDetail().isDeleted()) {
            throw new UsernameNotFoundException(COULD_NOT_LOG_USER_IN_ERROR_MESSAGE);
        }
        final String token = jwtTokenUtil.generateToken(user);
        UserLoginDTO loginDTO = mapUserToUserLoginDTO(user, token);
        logger.info(LOGIN_SUCCESSFUL_MESSAGE);
        return ResponseEntity.ok(loginDTO);
    }

    @Override
    public boolean register(UserRegistrationDTO registrationUser) {
        logger.info(String.format(REGISTERING_USER_MESSAGE, registrationUser.getUsername()));
        if (userRepository.existsByUsername(registrationUser.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }
        User user = new User();
        mapRegistrationDTOToUser(registrationUser, user);
        UserDetails userDetails = new UserDetails();
        mapRegistrationDTOToUserDetails(registrationUser, user, userDetails);
        logger.info(REGISTRATION_SUCCESSFUL_MESSAGE);
        return userRepository.save(user) != null;
    }

    @Override
    public boolean deleteUser(String username) {
        logger.info(String.format(DELETING_USER_MESSAGE, username));
        User user = userRepository.findByUsernameAndEnabledTrue(username);
        if (user == null || !user.isEnabled() || user.getUserDetail().isDeleted()) {
            throw new UserNotFoundException();
        }
        user.setEnabled(false);
        logger.info(DELETION_SUCCESSFUL_MESSAGE);
        return userRepository.save(user) != null;

    }

    @Override
    public boolean createUser(UserRegistrationDTO userRegistrationDTO) {
        logger.info(String.format(CREATING_USER_MESSAGE, userRegistrationDTO.getUsername()));
        if (userRepository.existsByUsername(userRegistrationDTO.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }
        UserDetails userDetails = new UserDetails();
        mapEditDTOtoUserDetails(userRegistrationDTO, userDetails);
        User user = new User();
        mapEditByAdminDTOtoUser(userRegistrationDTO, userDetails, user);
        System.out.println(user);
        logger.info(CREATION_SUCCESSFUL_MESSAGE);
        return userRepository.save(user) != null;
    }

    @Override
    public boolean editUser(UserEditDTO userEditDTO) {
        logger.info(String.format(EDITING_USER_MESSAGE, userEditDTO.getUsername()));
        if (!userEditDTO.getOldUsername().equals(userEditDTO.getUsername()) && userRepository.existsByUsername(userEditDTO.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }
        User user = userRepository.findByUsernameAndEnabledTrue(userEditDTO.getOldUsername());
        if (user == null || !user.isEnabled() || user.getUserDetail().isDeleted()) {
            throw new UserNotFoundException();
        }
        UserDetails userDetails = userDetailsRepository.findByIsDeletedFalseAndUser_Username(userEditDTO.getOldUsername());
        mapRegistrationDTOToUserDetails(userEditDTO, user, userDetails);
        logger.info(USER_SUCCESSFULLY_EDITED_MESSAGE);
        return userRepository.save(user) != null;
    }

    @Override
    public boolean avatarUpload(MultipartFile file, String username) throws IOException {
        logger.info(UPLOADING_AVATAR_MESSAGE);
        UserDetails userDetails = userRepository.findByUsernameAndEnabledTrue(username).getUserDetail();
        userDetails.setAvatar(file.getBytes());
        logger.info(AVATAR_UPLOADED_MESSAGE);
        return userDetailsRepository.save(userDetails) != null;
    }

    @Override
    public List<UserDisplayDTO> findAllByUsernameLike(String username) {
        logger.info(String.format(LOOKING_FOR_USERS_MESSAGE, username));
        List<User> users = userRepository.findAllByEnabledTrueAndUsernameLike(MATCHER + username + MATCHER);
        logger.info(USERS_FOUND_MESSAGE);
        return users.stream().map(ModelMapper::userToDTO).collect(Collectors.toList());
    }

    private String getAvatar(User user) {
        return user.getUserDetail().getAvatar() == null ? null : new String(Base64.encodeBase64(user.getUserDetail().getAvatar()));
    }

    private void mapRegistrationDTOToUserDetails(UserRegistrationDTO registrationUser, User user, UserDetails userDetails) {
        userDetails.setEmail(registrationUser.getEmail());
        userDetails.setFirstName(registrationUser.getFirstName());
        userDetails.setLastName(registrationUser.getLastName());
        userDetails.setUser(user);
        user.setUserDetail(userDetails);
    }

    private void mapRegistrationDTOToUser(UserRegistrationDTO registrationUser, User user) {
        user.setUsername(registrationUser.getUsername());
        if (user.getPassword() == null) {
            user.setPassword(passwordEncoder.encode(registrationUser.getPassword()));
        }
        if (user.getAuthority() == null) {
            user.setAuthority(authorityRepository.findById(1).orElse(null));
        }
        user.setEnabled(true);
    }


    private void mapEditByAdminDTOtoUser(UserRegistrationDTO userRegistrationDTO, UserDetails userDetails, User user) {
        user.setUsername(userRegistrationDTO.getUsername());
        if (userRegistrationDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        }
        user.setUserDetail(userDetails);
        userDetails.setUser(user);
        user.setAuthority(authorityRepository.findByName(userRegistrationDTO.getRole()));
        user.setEnabled(true);
    }

    private void mapEditDTOtoUserDetails(UserRegistrationDTO userRegistrationDTO, UserDetails userDetails) {
        userDetails.setFirstName(userRegistrationDTO.getFirstName());
        userDetails.setLastName(userRegistrationDTO.getLastName());
        userDetails.setEmail(userRegistrationDTO.getEmail());
        userDetails.setDeleted(false);
    }

    private UserLoginDTO mapUserToUserLoginDTO(User user, String token) {
        UserLoginDTO loginDTO = new UserLoginDTO();
        ModelMapper.setDetailsForUserDisplayDTO(user, loginDTO);
        loginDTO.setAvatar(getAvatar(user));
        loginDTO.setToken(token);
        return loginDTO;
    }
}
