package track_ninja.playlist_generator.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "authority")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Authority implements GrantedAuthority {

//    private static final String AUTHORITY_ID = "authority_id";
    private static final String ROLE_TYPE = "role_type";
//    static final String AUTHORITIES = "authorities";
    private static final String USERNAME = "username";

    @Id
    @Column(name = USERNAME)
    private String username;

    @Column(name = ROLE_TYPE)
    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthorityName name;

    @JsonIgnore
    @OneToMany(mappedBy = USERNAME)
    private Set<User> users;

    public AuthorityName getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getAuthority() {
        return name.toString();
    }

    public void setName(AuthorityName name) {
        this.name = name;
    }
}
