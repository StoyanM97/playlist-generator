package track_ninja.playlist_generator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
        return userService.getAll();
    }

    @GetMapping("/users/filter/{username}")
    private UserDisplayDTO getUser(@PathVariable String username){
        return userService.getUser(username);
    }

    @PostMapping("/create/user/")
    private boolean createUser(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO){
        return userService.createUser(userRegistrationDTO);
    }

    @PutMapping("/edit/user")
    private boolean editUserByAdmin(@Valid @RequestBody UserEditDTO userEditDTO){
        return userService.editUserByAdmin(userEditDTO);
    }

    @DeleteMapping("/delete/user/{username}")
    private boolean deleteUser(@PathVariable String username) {
        return userService.deleteUser(username);
    }
}
