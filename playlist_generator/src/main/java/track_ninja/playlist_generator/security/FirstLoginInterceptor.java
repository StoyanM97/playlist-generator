package track_ninja.playlist_generator.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import track_ninja.playlist_generator.models.User;
import track_ninja.playlist_generator.security.services.TokenService;
import track_ninja.playlist_generator.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class FirstLoginInterceptor implements HandlerInterceptor {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws IOException {
        User user = userService.getByUsername(tokenService.getUsernameFromToken(req));
        return !user.isFirstLogin();
    }
}
