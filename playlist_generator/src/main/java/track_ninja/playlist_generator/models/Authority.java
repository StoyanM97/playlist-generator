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

@Entity
@Table(name = "authorities")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Authority implements GrantedAuthority {

    private static final String AUTHORITY_ID = "authority_id";
    private static final String NAME = "name";
    static final String AUTHORITIES = "authorities";

    @Id
    @Column(name = AUTHORITY_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = NAME)
    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthorityName name;

    @JsonIgnore
    @ManyToMany(mappedBy = AUTHORITIES)
    private List<User> users;

    public AuthorityName getName() {
        return name;
    }

    @Override
    public String getAuthority() {
        return name.toString();
    }
}
