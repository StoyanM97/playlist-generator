package track_ninja.playlist_generator.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class UserDetails {

    private static final String USER_DETAILS_ID = "user_details_id";
    private static final String EMAIL = "email";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String AVATAR = "avatar";
    private static final String IS_DELETED = "is_deleted";
    private static final String USER_ID = "user_id";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = USER_DETAILS_ID)
    private int id;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = USER_ID)
    private User user;

    @Column(name = EMAIL)
    private String email;

    @Column(name = FIRST_NAME)
    private String firstName;

    @Column(name = LAST_NAME)
    private String lastName;

    @Column(name = AVATAR)
    private byte[] avatar;

    @Column(name = IS_DELETED)
    private boolean isDeleted;
}
