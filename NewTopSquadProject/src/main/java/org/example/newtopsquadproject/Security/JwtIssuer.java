package org.example.newtopsquadproject.Security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class JwtIssuer {

    private final JwtProperties jwtProperties;

    public JwtIssuer(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String issue(int userId, String email, List<String> roles){
        return JWT.create()
                .withSubject(String.valueOf(userId))
                .withExpiresAt(Instant.now().plus(Duration.of(3, ChronoUnit.DAYS)))
                .withClaim("e", email)
                .withClaim("a", roles)
                .sign(Algorithm.HMAC256(jwtProperties.getSecretKey()));
    }
}
