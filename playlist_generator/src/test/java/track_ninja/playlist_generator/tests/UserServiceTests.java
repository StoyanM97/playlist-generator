package track_ninja.playlist_generator.tests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import track_ninja.playlist_generator.exceptions.NoUsersCreatedException;
import track_ninja.playlist_generator.exceptions.UserNotFoundException;
import track_ninja.playlist_generator.models.User;
import track_ninja.playlist_generator.models.UserDetails;
import track_ninja.playlist_generator.repositories.AuthorityRepository;
import track_ninja.playlist_generator.repositories.UserDetailsRepository;
import track_ninja.playlist_generator.repositories.UserRepository;
import track_ninja.playlist_generator.security.models.JwtTokenUtil;
import track_ninja.playlist_generator.services.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTests {
    @Mock
    UserRepository userRepository;

    @Mock
    AuthorityRepository authorityRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private UserDetailsRepository userDetailsRepository;

    @InjectMocks
    UserServiceImpl userService;

    @Test(expected = NoUsersCreatedException.class)
    public void getAll_Should_ThrowNoUsersCreatedException_When_NoUsersCreated() {
        Mockito.when(userRepository.findAll()).thenReturn(new ArrayList<>());

        userService.getAll();
    }

    @Test
    public void getAll_Should_NotReturnUsers_When_UsersDeleted() {
        User user = new User();
        user.setEnabled(false);
        UserDetails userDetails = new UserDetails();
        userDetails.setDeleted(true);
        user.setUserDetail(userDetails);
        List<User> users = new ArrayList<>();
        users.add(user);

        Mockito.when(userRepository.findAll()).thenReturn(users);

        Assert.assertTrue("getAll returns users when they are deleted!", userService.getAll().isEmpty());
    }

    @Test(expected = UserNotFoundException.class)
    public void getUser_Should_ThrowUserNotFoundException_When_UserDoesNotExist() {
        String username = "testUser";

        Mockito.when(userRepository.findByUsernameAndEnabledTrue(username)).thenReturn(null);

        userService.getByUsername(username);
    }
}
