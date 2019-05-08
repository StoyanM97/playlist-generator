package track_ninja.playlist_generator.security.models;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import track_ninja.playlist_generator.models.Authority;
import track_ninja.playlist_generator.models.User;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;

@Component
public class JwtTokenUtil implements Serializable {

    private static final long ACCESS_TOKEN_VALIDITY_SECONDS = 1000*60*30; //30 mins
    private static final String SIGNING_KEY = "e5xJujg9qc5HCOnbBKHgfP2WmhrgIDYLblMoAvReSqKR2x3jq3fOoBgOK8zaKWM6";

    private static final String ISSUER = "http://devglan.com";
    private static final String SCOPES = "scopes";

    String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    private Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SIGNING_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(User user) {
        return doGenerateToken(user.getUsername(),user.getAuthority());
    }

    private String doGenerateToken(String subject, Authority role) {
        Claims claims = Jwts.claims().setSubject(subject);
        claims.put(SCOPES, Collections.singletonList(role.getName().toString()));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(ISSUER)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS))
                .signWith(SignatureAlgorithm.HS256, SIGNING_KEY)
                .compact();
    }

    Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (
                username.equals(userDetails.getUsername())
                        && !isTokenExpired(token));
    }
}
