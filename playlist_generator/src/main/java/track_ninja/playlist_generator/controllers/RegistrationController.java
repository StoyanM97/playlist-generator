package track_ninja.playlist_generator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import track_ninja.playlist_generator.models.dtos.UserRegistrationDTO;
import track_ninja.playlist_generator.services.UserService;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/register")
public class RegistrationController {

    private UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public boolean register(@Valid @RequestBody UserRegistrationDTO registrationUser) {
       return userService.register(registrationUser);
    }
}
