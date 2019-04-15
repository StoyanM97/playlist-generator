package track_ninja.playlist_generator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import track_ninja.playlist_generator.security.models.LoginUser;
import track_ninja.playlist_generator.services.UserService;

@RestController
@RequestMapping("api/register")
public class RegistrationController {

    private UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public void register(@RequestBody LoginUser loginUser) {
        userService.register(loginUser);
    }
}
