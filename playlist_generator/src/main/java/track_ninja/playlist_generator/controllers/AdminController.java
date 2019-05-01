package track_ninja.playlist_generator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import track_ninja.playlist_generator.exceptions.NoUsersCreatedException;
import track_ninja.playlist_generator.exceptions.UserNotFoundException;
import track_ninja.playlist_generator.exceptions.UsernameAlreadyExistsException;
import track_ninja.playlist_generator.models.dtos.UserDisplayDTO;
import track_ninja.playlist_generator.models.dtos.UserEditDTO;
import track_ninja.playlist_generator.models.dtos.UserRegistrationDTO;
import track_ninja.playlist_generator.services.UserService;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    private List<UserDisplayDTO> getAll(){
        try {
            return userService.getAll();
        } catch (NoUsersCreatedException ex) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, ex.getMessage());
        }
    }

    @GetMapping("/users/{username}")
    private List<UserDisplayDTO> getByUsername(@PathVariable String username) {
        try {
            return userService.findAllByUsernameLike(username);
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/users/filter/{username}")
    private UserDisplayDTO getUser(@PathVariable String username){
        try {
            return userService.getUser(username);
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @PostMapping("/create/user/")
    private boolean createUser(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO){
        try {
            return userService.createUser(userRegistrationDTO);
        } catch (UsernameAlreadyExistsException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, ex.getMessage());
        }
    }

    @PutMapping("/edit/user")
    private boolean editUserByAdmin(@Valid @RequestBody UserEditDTO userEditDTO){
        try {
            return userService.editUserByAdmin(userEditDTO);
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }  catch (UsernameAlreadyExistsException ex) {
        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, ex.getMessage());
    }
    }

    @DeleteMapping("/delete/user/{username}")
    private boolean deleteUser(@PathVariable String username) {
        try {
            return userService.deleteUser(username);
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }
}
