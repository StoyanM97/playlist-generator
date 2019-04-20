package track_ninja.playlist_generator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import track_ninja.playlist_generator.models.User;
import track_ninja.playlist_generator.services.UserService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    private Iterable<User> getAll(){
        return userService.getAll();
    }

    @GetMapping("/users/filter")
    private User getByUsername(@RequestParam String username){
        return userService.getByUsername(username);
    }

    @DeleteMapping("/user/delete")
    private void deleteUser(@RequestParam long userId) {
    }
}
