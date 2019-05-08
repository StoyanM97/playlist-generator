package track_ninja.playlist_generator.security.models;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String ERROR_GETTING_USERNAME_FROM_TOKEN_ERROR_MESSAGE = "An error occurred while getting username from token.";
    private static final String TOKEN_EXPIRED_ERROR_MESSAGE = "The token is expired and not valid anymore";
    private static final String AUTHENTICATION_FAILED_ERROR_MESSAGE = "Authentication failed. Username or password not valid.";
    private static final String COULDNT_FIND_BEARER_STRING_WILL_IGNORE_THE_HEADER_WARNING = "Couldn't find bearer string, will ignore the header.";
    private static final String AUTHENTICATED_USER_MESSAGE = "Authenticated user %s, setting security context";

    private static final String USER_SERVICE = "UserServiceImpl";

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER_STRING = "Authorization";
    private static final String DELIMITER = "";

    @Resource(name = USER_SERVICE)
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public JwtAuthenticationFilter() {
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(HEADER_STRING);
        String username = null;
        String authToken = null;

        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            authToken = header.replace(TOKEN_PREFIX, DELIMITER);
            try {
                username = jwtTokenUtil.getUsernameFromToken(authToken);
            } catch (IllegalArgumentException e) {
                logger.error(ERROR_GETTING_USERNAME_FROM_TOKEN_ERROR_MESSAGE, e);
           } catch (ExpiredJwtException e) {
                logger.warn(TOKEN_EXPIRED_ERROR_MESSAGE, e);
            } catch (SignatureException e) {
               logger.error(AUTHENTICATION_FAILED_ERROR_MESSAGE);
            }
        } else {
            logger.warn(COULDNT_FIND_BEARER_STRING_WILL_IGNORE_THE_HEADER_WARNING);
        }

        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if(jwtTokenUtil.validateToken(authToken, userDetails)){
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                logger.info(String.format(AUTHENTICATED_USER_MESSAGE, username));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}
