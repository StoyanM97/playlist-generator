package track_ninja.playlist_generator.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Entity
@Table(name = "user_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsModel {

    private static final String USER_DETAILS_ID = "user_details_id";
    private static final String EMAIL = "email";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String AVATAR = "avatar";
    private static final String IS_DELETED = "is_deleted";
    private static final String FIRST_NAME_LENGTH_ERROR_MESSAGE = "First name must be between 2 and 20 characters long!";
    private static final String LAST_NAME_LENGTH_ERROR_MESSAGE = "Last name must be between 2 and 20 characters long!";
    private static final int NAME_MIN_LENGTH = 2;
    private static final int NAME_MAX_LENGTH = 20;
//    private static final String USERNAME = "username";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = USER_DETAILS_ID)
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Email
    @Column(name = EMAIL)
    private String email;

    @Column(name = FIRST_NAME)
    @Size(min = NAME_MIN_LENGTH, max = NAME_MAX_LENGTH, message = FIRST_NAME_LENGTH_ERROR_MESSAGE)
    private String firstName;

    @Column(name = LAST_NAME)
    @Size(min = NAME_MIN_LENGTH, max = NAME_MAX_LENGTH, message = LAST_NAME_LENGTH_ERROR_MESSAGE)
    private String lastName;

    @Column(name = AVATAR)
    private byte[] avatar;

    @Column(name = IS_DELETED)
    private boolean isDeleted;
}
