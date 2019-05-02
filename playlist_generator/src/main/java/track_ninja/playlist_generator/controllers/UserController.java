package track_ninja.playlist_generator.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import track_ninja.playlist_generator.models.dtos.UserEditDTO;
import track_ninja.playlist_generator.exceptions.UserNotFoundException;
import track_ninja.playlist_generator.exceptions.UsernameAlreadyExistsException;
import track_ninja.playlist_generator.services.UserService;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }



    @PutMapping("/edit")
    private boolean editUser(@Valid @RequestBody UserEditDTO userEditDTO){
        try {
            return userService.editUser(userEditDTO);
        } catch (UserNotFoundException | UsernameAlreadyExistsException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, ex.getMessage());
        }
    }

    @PostMapping("/upload/avatar")
    public boolean avatarUpload(@RequestParam MultipartFile file, @RequestParam String username){

        try {
            return userService.avatarUpload(file, username);
        } catch (IOException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, ex.getMessage());
        }

    }
}
