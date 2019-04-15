package track_ninja.playlist_generator.security.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.Serializable;

public class LoginUser implements Serializable {
    private String username;
    private String password;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public LoginUser() {

    }

    public LoginUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
