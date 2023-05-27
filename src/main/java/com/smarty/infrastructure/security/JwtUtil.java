package com.smarty.infrastructure.security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

@Service
public class JwtUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${app.jwt.secret-key}")
    private String secretKey;

    @Value("${app.jwt.expiration}")
    private Long expiration;

    public String extractUsername(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();

        return (String) claims.get("username");
    }

    public Date extractExpiration(String jwt) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody().getExpiration();
    }

    public boolean validateToken(String jwt) {
        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(jwt);

            return true;
        } catch (ExpiredJwtException e) {
            LOGGER.error("Expired JWT token ", e);
        } catch (MalformedJwtException e) {
            LOGGER.error("Invalid JWT token ", e);
        } catch (UnsupportedJwtException e) {
            LOGGER.error("Unsupported JWT token ", e);
        } catch (IllegalArgumentException e) {
            LOGGER.error("JWT claims are missing ", e);
        }

        return false;
    }

    public String createToken(UserDetails user) {
        Date creationTime = new Date();
        Date expirationTime = new Date(creationTime.getTime() + expiration);

        var claims = new HashMap<String, Object>(0);
        user.getAuthorities().stream()
                .findFirst()
                .ifPresent(role -> claims.put("role", role.toString()));
        claims.put("username", user.getUsername());

        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(creationTime)
                .setClaims(claims)
                .setExpiration(expirationTime)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

}
