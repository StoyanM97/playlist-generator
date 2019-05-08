package track_ninja.playlist_generator.tests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import track_ninja.playlist_generator.exceptions.UserNotFoundException;
import track_ninja.playlist_generator.exceptions.UsernameAlreadyExistsException;
import track_ninja.playlist_generator.models.User;
import track_ninja.playlist_generator.models.UserDetails;
import track_ninja.playlist_generator.models.dtos.UserEditDTO;
import track_ninja.playlist_generator.models.dtos.UserRegistrationDTO;
import track_ninja.playlist_generator.repositories.UserRepository;
import track_ninja.playlist_generator.security.models.LoginUser;
import track_ninja.playlist_generator.services.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTests {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;



    @Test
    public void getAll_Should_NotReturnUsers_When_UsersDeleted() {
        User user = new User();
        user.setEnabled(false);
        UserDetails userDetails = new UserDetails();
        userDetails.setDeleted(true);
        user.setUserDetail(userDetails);
        List<User> users = new ArrayList<>();
        users.add(user);

        Mockito.when(userRepository.findAllByEnabledTrue()).thenReturn(users);

        Assert.assertTrue("getAll returns users when they are deleted!", userService.getAll().isEmpty());
    }

    @Test(expected = UserNotFoundException.class)
    public void getUser_Should_ThrowUserNotFoundException_When_UserDoesNotExist() {
        String username = "testUser";

        Mockito.when(userRepository.findByUsernameAndEnabledTrue(username)).thenReturn(null);

        userService.getByUsername(username);
    }

    @Test(expected = UserNotFoundException.class)
    public void getUser_Should_ThrowUserNotFoundException_When_UserIsNotEnabled() {
        String username = "testUser";
        User user = new User();
        user.setEnabled(false);

        Mockito.when(userRepository.findByUsernameAndEnabledTrue(username)).thenReturn(user);

        userService.getByUsername(username);
    }

    @Test(expected = UserNotFoundException.class)
    public void getUser_Should_ThrowUserNotFoundException_When_UserIsDeleted() {
        String username = "testUser";
        User user = new User();
        user.setEnabled(true);
        UserDetails userDetails = new UserDetails();
        userDetails.setDeleted(true);
        user.setUserDetail(userDetails);

        Mockito.when(userRepository.findByUsernameAndEnabledTrue(username)).thenReturn(user);

        userService.getByUsername(username);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void getLoggedUser_Should_ThrowUserNotFoundException_When_UserDoesNotExist() {
        LoginUser loginUser = new LoginUser();
        loginUser.setUsername("testUser");

        Mockito.when(userRepository.findByUsernameAndEnabledTrue(loginUser.getUsername())).thenReturn(null);

        userService.getLoggedUser(loginUser);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void getLoggedUser_Should_ThrowUserNotFoundException_When_UserIsNotEnabled() {
        LoginUser loginUser = new LoginUser();
        loginUser.setUsername("testUser");
        User user = new User();
        user.setEnabled(false);

        Mockito.when(userRepository.findByUsernameAndEnabledTrue(loginUser.getUsername())).thenReturn(user);

        userService.getLoggedUser(loginUser);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void getLoggedUser_Should_ThrowUserNotFoundException_When_UserIsDeleted() {
        LoginUser loginUser = new LoginUser();
        loginUser.setUsername("testUser");
        User user = new User();
        user.setEnabled(true);
        UserDetails userDetails = new UserDetails();
        userDetails.setDeleted(true);
        user.setUserDetail(userDetails);

        Mockito.when(userRepository.findByUsernameAndEnabledTrue(loginUser.getUsername())).thenReturn(user);

        userService.getLoggedUser(loginUser);
    }

    @Test(expected = UsernameAlreadyExistsException.class)
    public void registerUser_Should_ThrowUserAlreadyExistsException_When_UserAlreadyExists() {
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();
        userRegistrationDTO.setUsername("testUser");

        Mockito.when(userRepository.existsByUsername(userRegistrationDTO.getUsername())).thenReturn(true);

        userService.register(userRegistrationDTO);
    }

    @Test(expected = UserNotFoundException.class)
    public void deleteUser_Should_ThrowUserNotFoundException_When_UserDoesNotExist() {
        String username = "testUser";

        Mockito.when(userRepository.findByUsernameAndEnabledTrue(username)).thenReturn(null);

        userService.deleteUser(username);
    }

    @Test(expected = UserNotFoundException.class)
    public void deleteUser_Should_ThrowUserNotFoundException_When_UserIsNotEnabled() {
        String username = "testUser";
        User user = new User();
        user.setEnabled(false);

        Mockito.when(userRepository.findByUsernameAndEnabledTrue(username)).thenReturn(user);

        userService.deleteUser(username);
    }

    @Test(expected = UserNotFoundException.class)
    public void deleteUser_Should_ThrowUserNotFoundException_When_UserIsDeleted() {
        String username = "testUser";
        User user = new User();
        user.setEnabled(true);
        UserDetails userDetails = new UserDetails();
        userDetails.setDeleted(true);
        user.setUserDetail(userDetails);

        Mockito.when(userRepository.findByUsernameAndEnabledTrue(username)).thenReturn(user);

        userService.deleteUser(username);
    }

    @Test(expected = UsernameAlreadyExistsException.class)
    public void createUser_Should_ThrowUserAlreadyExistsException_When_UserAlreadyExists() {
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();
        userRegistrationDTO.setUsername("testUser");

        Mockito.when(userRepository.existsByUsername(userRegistrationDTO.getUsername())).thenReturn(true);

        userService.createUser(userRegistrationDTO);
    }

    @Test(expected = UsernameAlreadyExistsException.class)
    public void editUser_Should_ThrowUserAlreadyExistsException_When_UserAlreadyExists() {
        UserEditDTO userEditDTO = new UserEditDTO();
        userEditDTO.setUsername("testUserNew");
        userEditDTO.setOldUsername("testUserOld");

        Mockito.when(userRepository.existsByUsername(userEditDTO.getUsername())).thenReturn(true);

        userService.editUser(userEditDTO);
    }

    @Test(expected = UserNotFoundException.class)
    public void editUser_Should_ThrowUserNotFoundException_When_UserDoesNotExist() {
        UserEditDTO userEditDTO = new UserEditDTO();
        userEditDTO.setUsername("testUserNew");
        userEditDTO.setOldUsername("testUserOld");

        Mockito.when(userRepository.existsByUsername(userEditDTO.getUsername())).thenReturn(false);
        Mockito.when(userRepository.findByUsernameAndEnabledTrue(userEditDTO.getOldUsername())).thenReturn(null);

        userService.editUser(userEditDTO);
    }

    @Test(expected = UserNotFoundException.class)
    public void editUser_Should_ThrowUserNotFoundException_When_UserIsNotEnabled() {
        UserEditDTO userEditDTO = new UserEditDTO();
        userEditDTO.setUsername("testUserNew");
        userEditDTO.setOldUsername("testUserOld");
        User user = new User();
        user.setEnabled(false);

        Mockito.when(userRepository.existsByUsername(userEditDTO.getUsername())).thenReturn(false);
        Mockito.when(userRepository.findByUsernameAndEnabledTrue(userEditDTO.getOldUsername())).thenReturn(user);

        userService.editUser(userEditDTO);
    }

    @Test(expected = UserNotFoundException.class)
    public void editUser_Should_ThrowUserNotFoundException_When_UserIsDeleted() {
        UserEditDTO userEditDTO = new UserEditDTO();
        userEditDTO.setUsername("testUserNew");
        userEditDTO.setOldUsername("testUserOld");
        User user = new User();
        user.setEnabled(true);
        UserDetails userDetails = new UserDetails();
        userDetails.setDeleted(true);
        user.setUserDetail(userDetails);

        Mockito.when(userRepository.existsByUsername(userEditDTO.getUsername())).thenReturn(false);
        Mockito.when(userRepository.findByUsernameAndEnabledTrue(userEditDTO.getOldUsername())).thenReturn(user);

        userService.editUser(userEditDTO);
    }
}
