package com.example.demo.config;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret.api}")
    private String secretKey;

    @Value("${jwt.expiration-ms}")
    private long tokenExpiry;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token); // this will throw exceptions if invalid
            return true;
        } catch (ExpiredJwtException e) {
            System.err.println("JWT Token has expired: " + e.getMessage());
        } catch (SignatureException e) {
            System.err.println("JWT Signature is invalid: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.err.println("JWT Token is malformed: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error validating JWT Token: " + e.getMessage());
        }
        return false;
    }

    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claims != null ? claimsResolver.apply(claims) : null;
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            System.err.println("Error extracting claims from JWT Token: " + e.getMessage());
            return null;
        }
    }
}
