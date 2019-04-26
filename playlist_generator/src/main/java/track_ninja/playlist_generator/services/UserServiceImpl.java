package track_ninja.playlist_generator.services;

import org.apache.tomcat.util.codec.binary.Base64;
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
import track_ninja.playlist_generator.exceptions.NoUsersCreatedException;
import track_ninja.playlist_generator.exceptions.UserNotFoundException;
import track_ninja.playlist_generator.exceptions.UsernameAlreadyExistsException;
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
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsernameAndEnabledTrue(username);
    }

    @Override
    public List<UserDisplayDTO> getAll() {
        List<UserDisplayDTO> userDisplayDTOS = new ArrayList<>();
        Iterable<User> users = userRepository.findAll();
        if (!users.iterator().hasNext()) {
            throw new NoUsersCreatedException();
        }
        users.forEach(user -> {
            if(!user.getUserDetail().isDeleted()){
                userDisplayDTOS.add(ModelMapper.userToDTO(user));
            }
        });
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
        if(user.getUserDetail().isDeleted()){
            throw new UserNotFoundException();
        }
        return user;
    }

    @Override
    public ResponseEntity getLoggedUser(LoginUser loginUser){

        final User user = userRepository.findByUsernameAndEnabledTrue((loginUser.getUsername()));
        final String token = jwtTokenUtil.generateToken(user);
        UserLoginDTO loginDTO = mapUserToUserLoginDTO(user, token);

        return ResponseEntity.ok(loginDTO);
    }

    @Override
    public boolean register(UserRegistrationDTO registrationUser) {
        if (userRepository.existsByUsername(registrationUser.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }
        User user = new User();
        mapRegistrationDTOToUser(registrationUser, user);
        UserDetails userDetails = new UserDetails();
        mapRegistrationDTOToUserDetails(registrationUser, user, userDetails);
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
        UserDetails userDetails = new UserDetails();
        mapCreateEditAdminDTOtoUserDetails(userRegistrationDTO, userDetails);
        User user = new User();
        mapCreateEditUserByAdminDTOtoUser(userRegistrationDTO, userDetails, user);
        return userRepository.save(user) != null;
    }

    @Override
    public boolean editUserByAdmin(UserEditDTO userEditDTO) {
        if (userRepository.existsByUsername(userEditDTO.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }
        User user = getByUsername(userEditDTO.getOldUsername());
        if (user == null) {
            throw new UserNotFoundException();
        }
        UserDetails userDetails = userDetailsRepository.findByIsDeletedFalseAndUser_Username(userEditDTO.getUsername());
        mapCreateEditAdminDTOtoUserDetails(userEditDTO, userDetails);
        mapCreateEditUserByAdminDTOtoUser(userEditDTO, userDetails, user);
        return userRepository.save(user) != null;
    }

    @Override
    public boolean editUser(UserEditDTO userEditDTO) {
        if (userRepository.existsByUsername(userEditDTO.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }
        User user = userRepository.findByUsernameAndEnabledTrue(userEditDTO.getUsername());
        if (user == null) {
            throw new UserNotFoundException();
        }
        mapRegistrationDTOToUser(userEditDTO, user);
        UserDetails userDetails =userDetailsRepository.findByIsDeletedFalseAndUser_Username(userEditDTO.getUsername());
        mapRegistrationDTOToUserDetails(userEditDTO, user, userDetails);

        return userRepository.save(user) != null;
    }

    @Override
    public boolean avatarUpload(MultipartFile file, String username) {
        return false;
    }

    private String getAvatar(User user){
        return user.getUserDetail().getAvatar()==null? null : new String(Base64.encodeBase64(user.getUserDetail().getAvatar()));
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
        user.setPassword(passwordEncoder.encode(registrationUser.getPassword()));
        user.setAuthority(authorityRepository.findById(1).orElse(null));
        user.setEnabled(true);
    }

    private void mapCreateEditUserByAdminDTOtoUser(UserRegistrationDTO userRegistrationDTO, UserDetails userDetails, User user) {
        user.setUsername(userRegistrationDTO.getUsername());
        user.setPassword(userRegistrationDTO.getPassword());
        user.setUserDetail(userDetails);
        user.setAuthority(authorityRepository.findByName(userRegistrationDTO.getRole()));
        user.setEnabled(true);
    }

    private void mapCreateEditAdminDTOtoUserDetails(UserRegistrationDTO userRegistrationDTO, UserDetails userDetails) {
        userDetails.setFirstName(userRegistrationDTO.getFirstName());
        userDetails.setLastName(userRegistrationDTO.getLastName());
        userDetails.setEmail(userRegistrationDTO.getEmail());
        userDetails.setDeleted(false);
    }

    private UserLoginDTO mapUserToUserLoginDTO(User user, String token) {
        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setUsername(user.getUsername());
        loginDTO.setRole(user.getAuthority().getName().toString());
        loginDTO.setFirstName(user.getUserDetail().getFirstName());
        loginDTO.setLastName(user.getUserDetail().getLastName());
        loginDTO.setEmail(user.getUserDetail().getEmail());
        loginDTO.setAvatar(getAvatar(user));
        loginDTO.setToken(token);
        return loginDTO;
    }
}
