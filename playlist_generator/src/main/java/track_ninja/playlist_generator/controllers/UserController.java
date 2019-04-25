package track_ninja.playlist_generator.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import track_ninja.playlist_generator.models.dtos.UserEditDTO;
import track_ninja.playlist_generator.services.UserService;

import javax.validation.Valid;

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
        return userService.editUser(userEditDTO);
    }

    @PostMapping("/upload/avatar")
    public boolean avatarUpload(@RequestParam MultipartFile file, @RequestParam String username) {

        return userService.avatarUpload(file, username);

    }
}
