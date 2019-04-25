package track_ninja.playlist_generator.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDTO extends UserDTO{

    private static final int PASSWORD_MIN_LENGTH = 8;
    private static final int PASSWORD_MAX_LENGTH = 16;
    private static final String PASSWORD_LENGTH_ERROR_MESSAGE = "Password must be at least 8 and at most 16 characters long!";


    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH, message = PASSWORD_LENGTH_ERROR_MESSAGE)
    private String password;



}
