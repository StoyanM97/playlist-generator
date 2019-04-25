package track_ninja.playlist_generator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import track_ninja.playlist_generator.security.models.LoginUser;
import track_ninja.playlist_generator.services.UserService;

import javax.validation.Valid;


@RestController
@RequestMapping("/api")
public class LoginController {

    private UserService userService;
    private AuthenticationManager authenticationManager;

    @Autowired
    public LoginController(UserService userService, AuthenticationManager authenticationManager) {

        this.userService = userService;
        this.authenticationManager = authenticationManager;

    }
    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody LoginUser loginUser){
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return userService.getLoggedUser(loginUser);
    }

}
