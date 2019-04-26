package track_ninja.playlist_generator.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User implements org.springframework.security.core.userdetails.UserDetails {

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

    @Column(name = USER_NAME)
    private String username;

    @Column(name = PASSWORD)
    private String password;

    @Column(name = ENABLED)
    private boolean enabled;

    @OneToOne(mappedBy = USER, cascade = CascadeType.ALL)
    private UserDetails userDetail;

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
