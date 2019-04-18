package track_ninja.playlist_generator.controllers;

import org.h2.security.auth.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import track_ninja.playlist_generator.models.User;
import track_ninja.playlist_generator.security.models.AuthToken;
import track_ninja.playlist_generator.security.models.JwtTokenUtil;
import track_ninja.playlist_generator.security.models.LoginUser;
import track_ninja.playlist_generator.security.services.TokenService;
import track_ninja.playlist_generator.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class AuthenticationController {
    private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwtTokenUtil;
    private UserService userService;
//    private TokenService<HttpServletRequest> tokenService;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserService userService, TokenService<HttpServletRequest> tokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
//        this.tokenService = tokenService;
    }
    @PostMapping("/login")
    public ResponseEntity register(@RequestBody LoginUser loginUser, HttpServletResponse res) throws AuthenticationException {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final User user = userService.getByUsername(loginUser.getUsername());
        final String token = jwtTokenUtil.generateToken(user);
        return ResponseEntity.ok(new AuthToken(token, user.getAuthority().getName().toString(), user.isFirstLogin()));
    }

//    @PutMapping("/reset")
//    public void changePassword(@RequestBody  Map<String,String> password, HttpServletRequest req){
//        String p = password.get("password");
//        int adminId = userService.getIdByUsername(tokenService.getUsernameFromToken(req));
//        adminService.changePassword(adminId,p);
//    }
}
