package track_ninja.playlist_generator.models.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDisplayDTO {

    private String username;
    private String role;
    private String firstName;
    private String lastName;
    private String email;
    private String avatar;
}
