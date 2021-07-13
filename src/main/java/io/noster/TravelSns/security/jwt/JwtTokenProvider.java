package io.noster.TravelSns.security.jwt;

import com.auth0.jwt.JWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;

import java.nio.file.attribute.UserPrincipal;
import java.util.Date;
import java.util.Properties;

public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    public String generateTokenWithPrefix(Authentication authentication) {
        return Properties.TOKEN_PREFIX + generateToken(((UserPrincipal) authentication.getPrincipal()));
    }

    public String generateTokenWithPrinciple(UserPrincipal userPrincipal) {
        return generateToken(userPrincipal);
    }

    public String generateToken(UserPrincipal userPrincipal) {
        return JWT.create()
                .withSubject(userPrincipal.getUsername())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + Properties.EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(Properties.SECRET.getBytes()));
    }

    public String getUserNameFromJWT(String token) {
        return JWT.require(Algorithm.HMAC256(Properties.SECRET.getByte()))
                .build()
                .verify(token)
                .getSubject();
    }


    public boolean validateToken(String authToken) {
        try {
            JWT.require(Algorithm.HMAC256(Properties.SECRET.getBytes())).build().verify(authToken);
            return true;
        } catch (JWTVerificationException ex) {
            logger.error(ex.getLocalizedMessage());
            logger.error("JWT claims string is empty");
        }
        return false;
    }
}
