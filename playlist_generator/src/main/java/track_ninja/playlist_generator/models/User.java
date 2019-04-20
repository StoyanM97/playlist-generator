package track_ninja.playlist_generator.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    //    private static final String FIRST_NAME_LENGTH_ERROR_MESSAGE = "First name must be at least 2 characters and at most 15 characters long!";
//    private static final String LAST_NAME_LENGTH_ERROR_MESSAGE = "Last name must be at least 2 characters and at most 15 characters long!";
//    private static final int NAME_MIN_LENGTH = 2;
//    private static final int NAME_MAX_LENGTH = 15;
//
//    private static final String EMAIL_ADDRESS_ERROR_MESSAGE = "Invalid Email!";
//
//    private static final String USER_ID = "user_id";
//    private static final String FIRST_NAME = "first_name";
//    private static final String LAST_NAME = "last_name";
//    private static final String EMAIL_ADDRESS = "email";
//    private static final String AVATAR = "avatar";
//    private static final String IS_USER_DELETED = "is_deleted";
    private static final int USER_NAME_MIN_LENGTH = 5;
    private static final int USER_NAME_MAX_LENGTH = 10;
    private static final String USER_NAME_LENGTH_ERROR_MESSAGE = "User name must be at least 5 and at most 10 characters long!";
    private static final int PASSWORD_MIN_LENGTH = 6;
    private static final int PASSWORD_MAX_LENGTH = 15;
    private static final String PASSWORD_LENGTH_ERROR_MESSAGE = "Password must be at least 6 and at most 15 characters long!";
    private static final String USER_NAME = "username";
    private static final String PASSWORD = "password";
    private static final String USER = "user";
    private static final String ENABLED = "enabled";
    static final String USERNAME = "username";
    private static final String AUTHORITY_ID = "authority_id";


    @Id
    @Size(min = USER_NAME_MIN_LENGTH, max = USER_NAME_MAX_LENGTH, message = USER_NAME_LENGTH_ERROR_MESSAGE)
    @Column(name = USER_NAME)
    private String username;

//    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH, message = PASSWORD_LENGTH_ERROR_MESSAGE)
    @Column(name = PASSWORD)
    private String password;

//    @Column(name = AVATAR)
//    private byte[] avatar;

    @Column(name = ENABLED)
    private boolean enabled;

    private boolean isFirstLogin;

    @ManyToOne
    @JoinColumn(name = AUTHORITY_ID, insertable = false, updatable = false)
    private Authority authority;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Authority> getAuthorities() {
        return Collections.singletonList(authority);
    }

    public boolean isFirstLogin() {
        return isFirstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        isFirstLogin = firstLogin;
    }

    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }
}
