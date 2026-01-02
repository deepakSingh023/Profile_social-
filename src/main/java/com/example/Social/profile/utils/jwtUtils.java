package com.example.Social.profile.utils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Component
public class jwtUtils {

    @Value("${JWT_SECRET}")
    private String SECRET;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            System.out.println("❌ JWT validation failed: " + ex.getMessage());
            return false;
        }
    }

    // ✅ Extract userId (UUID) from token
    public UUID extractUserId(String token) {
        return UUID.fromString(getClaims(token).getSubject());
    }

    // ✅ Extract email claim
    public String extractEmail(String token) {
        return getClaims(token).get("email", String.class);
    }

    // ✅ Extract role claim
    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    // ✅ Extract expiration date
    public Date extractExpiration(String token) {
        return getClaims(token).getExpiration();
    }

    // Internal method to parse claims
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
