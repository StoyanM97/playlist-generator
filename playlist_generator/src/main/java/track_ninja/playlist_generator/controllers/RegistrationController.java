package track_ninja.playlist_generator.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import track_ninja.playlist_generator.exceptions.UsernameAlreadyExistsException;
import track_ninja.playlist_generator.models.dtos.UserRegistrationDTO;
import track_ninja.playlist_generator.services.UserService;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/register")
public class RegistrationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationController.class);

    private UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public boolean register(@Valid @RequestBody UserRegistrationDTO registrationUser) {
        try {
            return userService.register(registrationUser);
        } catch (UsernameAlreadyExistsException ex) {
            LOGGER.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, ex.getMessage());
        }
    }
}
