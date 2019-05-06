package track_ninja.playlist_generator.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import track_ninja.playlist_generator.models.commons.UserRole;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class UserDTO {

    private static final int USER_NAME_MIN_LENGTH = 5;
    private static final int USER_NAME_MAX_LENGTH = 10;
    private static final String USER_NAME_LENGTH_ERROR_MESSAGE = "User name must be at least 5 and at most 10 characters long!";

    private static final String FIRST_NAME_LENGTH_ERROR_MESSAGE = "First name must be between 2 and 20 characters long!";
    private static final String LAST_NAME_LENGTH_ERROR_MESSAGE = "Last name must be between 2 and 20 characters long!";
    private static final int NAME_MIN_LENGTH = 2;
    private static final int NAME_MAX_LENGTH = 20;

    @NotNull
    @Size(min = USER_NAME_MIN_LENGTH, max = USER_NAME_MAX_LENGTH, message = USER_NAME_LENGTH_ERROR_MESSAGE)
    private String username;

    @Email
    private String email;

    @Size(min = NAME_MIN_LENGTH, max = NAME_MAX_LENGTH, message = FIRST_NAME_LENGTH_ERROR_MESSAGE)
    private String firstName;

    @Size(min = NAME_MIN_LENGTH, max = NAME_MAX_LENGTH, message = LAST_NAME_LENGTH_ERROR_MESSAGE)
    private String lastName;

    @Enumerated(EnumType.STRING)
    private UserRole role;
}
