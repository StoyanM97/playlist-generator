package track_ninja.playlist_generator.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private static final String FIRST_NAME_LENGTH_ERROR_MESSAGE = "First name must be at least 2 characters and at most 15 characters long!";
    private static final String LAST_NAME_LENGTH_ERROR_MESSAGE = "Last name must be at least 2 characters and at most 15 characters long!";
    private static final int NAME_MIN_LENGTH = 2;
    private static final int NAME_MAX_LENGTH = 15;

    private static final String EMAIL_ADDRESS_ERROR_MESSAGE = "Invalid Email!";

    private static final String USER_ID = "UserId";
    private static final String FIRST_NAME = "FirstName";
    private static final String LAST_NAME = "LastName";
    private static final String EMAIL_ADDRESS = "Email";
    private static final String AVATAR = "Avatar";
    private static final String IS_USER_DELETED = "IsDeleted";
    private static final int USER_NAME_MIN_LENGTH = 5;
    private static final int USER_NAME_MAX_LENGTH = 10;
    private static final String USER_NAME_LENGTH_ERROR_MESSAGE = "User name must be at least 5 and at most 10 characters long!";
    private static final int PASSWORD_MIN_LENGTH = 6;
    private static final int PASSWORD_MAX_LENGTH = 15;
    private static final String PASSWORD_LENGTH_ERROR_MESSAGE = "Password must be at least 6 and at most 15 characters long!";
    private static final String USER_NAME = "UserName";
    private static final String PASSWORD = "Password";
    private static final String USER = "user";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = USER_ID)
    private Long id;

    @Size(min = NAME_MIN_LENGTH, max = NAME_MAX_LENGTH, message = FIRST_NAME_LENGTH_ERROR_MESSAGE)
    @Column(name = FIRST_NAME)
    private String firstName;


    @Size(min = NAME_MIN_LENGTH, max = NAME_MAX_LENGTH, message = LAST_NAME_LENGTH_ERROR_MESSAGE)
    @Column(name = LAST_NAME)
    private String lastName;

    @Email(message = EMAIL_ADDRESS_ERROR_MESSAGE)
    @Column(name = EMAIL_ADDRESS)
    private String email;

    @Size(min = USER_NAME_MIN_LENGTH, max = USER_NAME_MAX_LENGTH, message = USER_NAME_LENGTH_ERROR_MESSAGE)
    @Column(name = USER_NAME)
    private String username;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH, message = PASSWORD_LENGTH_ERROR_MESSAGE)
    @Column(name = PASSWORD)
    private String password;

//    @Column(name = AVATAR)
//    private byte[] avatar;

    @Column(name = AVATAR)
    private String avatar;

    @Column(name = IS_USER_DELETED)
    private boolean isDeleted;

    @OneToMany(mappedBy = USER)
    private Set<Playlist> playLists;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_authority_relations",
            joinColumns = {@JoinColumn(name = "UserId", referencedColumnName = "UserId")},
            inverseJoinColumns = {@JoinColumn(name = "AuthorityId", referencedColumnName = "AuthorityId")})
    private List<Authority> authorities;
}
