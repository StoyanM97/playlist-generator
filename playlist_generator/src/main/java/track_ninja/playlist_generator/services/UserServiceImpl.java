package track_ninja.playlist_generator.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import track_ninja.playlist_generator.models.Authority;
import track_ninja.playlist_generator.models.AuthorityName;
import track_ninja.playlist_generator.models.User;
import track_ninja.playlist_generator.repositories.AuthorityRepository;
import track_ninja.playlist_generator.repositories.UserRepository;
import track_ninja.playlist_generator.security.models.LoginUser;

import java.util.List;

@Service("UserServiceImpl")
public class UserServiceImpl implements UserService, UserDetailsService {
    private UserRepository userRepository;
    private AuthorityRepository authorityRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
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
    public void register(LoginUser loginUser) {
        User user = new User();
        user.setUsername(loginUser.getUsername());
        user.setPassword(passwordEncoder.encode(loginUser.getPassword()));
        user.setAuthority(authorityRepository.findById(1L).orElse(null));
        user.setEnabled(true);
        user.setFirstLogin(true);
        userRepository.save(user);
    }

    @Override
    public void delete(String username) {
        User user = userRepository.findByUsername(username);
        user.setEnabled(false);
        userRepository.save(user);
    }
}
