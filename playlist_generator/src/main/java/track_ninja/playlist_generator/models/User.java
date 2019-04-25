package track_ninja.playlist_generator.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {


    private static final int USER_NAME_MIN_LENGTH = 5;
    private static final int USER_NAME_MAX_LENGTH = 10;
    private static final String USER_NAME_LENGTH_ERROR_MESSAGE = "User name must be at least 5 and at most 10 characters long!";

    private static final int PASSWORD_MIN_LENGTH = 8;
    private static final int PASSWORD_MAX_LENGTH = 16;
    private static final String PASSWORD_LENGTH_ERROR_MESSAGE = "Password must be at least 6 and at most 15 characters long!";

    private static final String USER_ID = "user_id";
    private static final String USER_NAME = "username";
    private static final String PASSWORD = "password";
    private static final String USER = "user";
    private static final String ENABLED = "enabled";
    private static final String AUTHORITY_ID = "authority_id";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = USER_ID)
    private int userId;

    @Size(min = USER_NAME_MIN_LENGTH, max = USER_NAME_MAX_LENGTH, message = USER_NAME_LENGTH_ERROR_MESSAGE)
    @Column(name = USER_NAME)
    private String username;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH, message = PASSWORD_LENGTH_ERROR_MESSAGE)
    @Column(name = PASSWORD)
    private String password;

    @Column(name = ENABLED)
    private boolean enabled;

    @OneToOne(mappedBy = USER, cascade = CascadeType.ALL)
    private UserDetailsModel userDetail;

    @ManyToOne
    @JoinColumn(name = AUTHORITY_ID)
    private Authority authority;


    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public List<Authority> getAuthorities() {
        return Collections.singletonList(authority);
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


}
