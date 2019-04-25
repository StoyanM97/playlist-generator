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
import track_ninja.playlist_generator.models.dtos.CreateEditUserByAdminDTO;
import track_ninja.playlist_generator.models.dtos.LoginUserDTO;
import track_ninja.playlist_generator.models.dtos.RegistrationDTO;
import track_ninja.playlist_generator.repositories.AuthorityRepository;
import track_ninja.playlist_generator.repositories.UserRepository;
import track_ninja.playlist_generator.security.models.JwtTokenUtil;
import track_ninja.playlist_generator.security.models.LoginUser;

@Service("UserServiceImpl")
public class UserServiceImpl implements UserService, UserDetailsService {

    private UserRepository userRepository;
    private AuthorityRepository authorityRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                           AuthorityRepository authorityRepository, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    @Override
    public Iterable<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getByUsername(String username) {

        return userRepository.findByUsername(username);
    }

    @Override
    public ResponseEntity getLoggedUser(LoginUser loginUser){

        final User user = getByUsername(loginUser.getUsername());
        final String token = jwtTokenUtil.generateToken(user);
        return ResponseEntity.ok(new LoginUserDTO(user.getUsername(),user.getAuthority().getName().toString(),
                user.getUserDetail().getFirstName(), user.getUserDetail().getLastName(), user.getUserDetail().getEmail(),
                getAvatar(user),token));
    }

    @Override
    public boolean register(RegistrationDTO registrationUser) {
        User user = new User();
        user.setUsername(registrationUser.getUsername());
        user.setPassword(passwordEncoder.encode(registrationUser.getPassword()));
        user.setAuthority(authorityRepository.findById(1).orElse(null));
        user.setEnabled(true);
        UserDetailsModel userDetailsModel = new UserDetailsModel();
        userDetailsModel.setEmail(registrationUser.getEmail());
        userDetailsModel.setFirstName(registrationUser.getFirstName());
        userDetailsModel.setLastName(registrationUser.getLastName());
        userDetailsModel.setUser(user);
        user.setUserDetail(userDetailsModel);
        return userRepository.save(user) != null;
    }

    @Override
    public boolean deleteUser(String username) {
        User user = userRepository.findByUsername(username);
        user.setEnabled(false);
       return userRepository.save(user) != null;

    }

    @Override
    public boolean createUser(CreateEditUserByAdminDTO createEditUserByAdminDTO) {
        //he can set the user role. The dto has field UserRole. Should be valid
        return false;
    }

    @Override
    public boolean editUserByAdmin(CreateEditUserByAdminDTO createEditUserByAdminDTO) {
        //he can edit the user role. The dto has field UserRole. Should be valid
        return false;
    }

    @Override
    public boolean editUser(RegistrationDTO registrationDTO) {
        return false;
    }

    @Override
    public boolean avatarUpload(MultipartFile file, String username) {
        return false;
    }

    private String getAvatar(User user){
        return user.getUserDetail().getAvatar()==null? "" : new String(Base64.encodeBase64(user.getUserDetail().getAvatar()));
    }
}
