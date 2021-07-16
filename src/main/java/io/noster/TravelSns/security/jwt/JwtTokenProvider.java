package io.noster.TravelSns.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import io.noster.TravelSns.model.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Properties;

public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private String expiration;

    public String generateTokenWithPrefix(Authentication authentication) {
        return "Authorization" + generateToken(((UserPrincipal) authentication.getPrincipal()));
    }

    public String generateTokenWithPrinciple(UserPrincipal userPrincipal) {
        return generateToken(userPrincipal);
    }

    public String generateToken(UserPrincipal userPrincipal) {
        logger.info("Print time " + expiration);

        logger.info("User data " + userPrincipal.getUsername());
        String jwtss = JWT.create()
                .withSubject(userPrincipal.getUsername())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date((new Date()).getTime() + expiration))
                .sign(Algorithm.HMAC256(secret.getBytes()));

        logger.info("User jwt " + jwtss);

        return jwtss;
    }

    public String getUserNameFromJWT(String token) {
        return JWT.require(Algorithm.HMAC256(secret.getBytes()))
                .build()
                .verify(token)
                .getSubject();
    }


    public boolean validateToken(String authToken) {
        try {
            JWT.require(Algorithm.HMAC256(secret.getBytes())).build().verify(authToken);
            return true;
        } catch (JWTVerificationException ex) {
            logger.error(ex.getLocalizedMessage());
            logger.error("JWT claims string is empty");
        }
        return false;
    }
}
