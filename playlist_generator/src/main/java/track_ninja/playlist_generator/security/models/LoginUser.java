package track_ninja.playlist_generator.security.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser implements Serializable {

    private String username;
    private String password;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

}
