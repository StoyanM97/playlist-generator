package track_ninja.playlist_generator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import track_ninja.playlist_generator.models.dtos.CreateEditUserByAdminDTO;
import track_ninja.playlist_generator.models.dtos.UserDTO;
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
    private List<UserDTO> getAll(){
        return userService.getAll();
    }

    @GetMapping("/users/filter")
    private UserDTO getUser(@RequestBody String username){
        return userService.getUser(username);
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
    private boolean deleteUser(@RequestBody String username) {

        return userService.deleteUser(username);
    }
}
