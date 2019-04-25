package track_ninja.playlist_generator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import track_ninja.playlist_generator.models.User;
import track_ninja.playlist_generator.models.dtos.CreateEditUserByAdminDTO;
import track_ninja.playlist_generator.services.UserService;

import javax.validation.Valid;


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

    @PostMapping("/create/user/")
    private boolean createUser(@Valid @RequestBody CreateEditUserByAdminDTO createEditUserByAdminDTO){
        return userService.createUser(createEditUserByAdminDTO);
    }

    @PutMapping("/edit/user")
    private boolean editUserByAdmin(@Valid @RequestBody CreateEditUserByAdminDTO createEditUserByAdminDTO){
        return userService.editUserByAdmin(createEditUserByAdminDTO);
    }

    @DeleteMapping("/delete/user")
    private boolean deleteUser(@RequestParam String username) {
        return userService.deleteUser(username);
    }
}
