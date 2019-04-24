package track_ninja.playlist_generator.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserDTO {

    private String username;
    private String role;
    private String firstName;
    private String lastName;
    private String email;
    private String avatar;
    private String token;

}
