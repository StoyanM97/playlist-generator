package track_ninja.playlist_generator.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import track_ninja.playlist_generator.models.commons.UserRole;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "authorities")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Authority implements GrantedAuthority {

    private static final String ROLE_TYPE = "role_type";
    private static final String AUTHORITY_ID = "authority_id";
    private static final String AUTHORITY = "authority";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = AUTHORITY_ID)
    private int authorityId;

    @Column(name = ROLE_TYPE)
    @NotNull
    @Enumerated(EnumType.STRING)
    private UserRole name;

    @JsonIgnore
    @OneToMany(mappedBy = AUTHORITY)
    private Set<User> users;

    @Override
    public String getAuthority() {
        return name.toString();
    }
}
