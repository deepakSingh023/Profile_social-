package com.example.Social.profile.utils;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class jwtUtils {

    @Value("${JWT_SECRET}")
    private String SECRET;

    // ✅ Validate token signature + expiration
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token); // will throw exception if invalid
            return true;
        } catch (ExpiredJwtException ex) {
            System.out.println("❌ Token expired: " + ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            System.out.println("❌ Unsupported JWT: " + ex.getMessage());
        } catch (MalformedJwtException ex) {
            System.out.println("❌ Malformed JWT: " + ex.getMessage());
        } catch (SignatureException ex) {
            System.out.println("❌ Invalid signature: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            System.out.println("❌ Token is null/empty: " + ex.getMessage());
        }
        return false;
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
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
}
