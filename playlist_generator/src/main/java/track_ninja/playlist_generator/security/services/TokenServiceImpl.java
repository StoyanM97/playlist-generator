package track_ninja.playlist_generator.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import track_ninja.playlist_generator.security.models.JwtTokenUtil;

import javax.servlet.http.HttpServletRequest;

import static track_ninja.playlist_generator.security.Constants.HEADER_STRING;
import static track_ninja.playlist_generator.security.Constants.TOKEN_PREFIX;

@Service
public class TokenServiceImpl implements TokenService<HttpServletRequest> {
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    public TokenServiceImpl(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public String getUsernameFromToken(HttpServletRequest req) {
        String header = req.getHeader(HEADER_STRING);
        String username = null;
        if (header.startsWith(TOKEN_PREFIX)) {
            String authToken = header.replace(TOKEN_PREFIX, "");
            try {
                username = jwtTokenUtil.getUsernameFromToken(authToken);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("invalid token prefix");
            throw new NullPointerException();
        }
        return username;
    }
}
