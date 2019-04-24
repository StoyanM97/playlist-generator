package track_ninja.playlist_generator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import track_ninja.playlist_generator.security.models.LoginUser;
import track_ninja.playlist_generator.services.UserService;



@RestController
@RequestMapping("/api/register")
public class RegistrationController {

    private UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public void register(@RequestBody LoginUser loginUser) {
        System.out.println(loginUser.getUsername() + loginUser.getPassword());
        userService.register(loginUser);
    }
}
