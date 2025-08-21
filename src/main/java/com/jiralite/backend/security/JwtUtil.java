package com.jiralite.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;
@Component
public class JwtUtil {

    private static final String SECRET = "mysupersecretkeymysupersecretkeymysupersecretkey!";
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());
    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour

    public String generateToken(String email, String orgId, String designation, String fullName) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("orgId", orgId);
        claims.put("designation", designation);
        claims.put("fullName", fullName);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractEmail(String token) { return extractClaims(token).getSubject(); }
    public String extractOrgId(String token) { return (String) extractClaims(token).get("orgId"); }
    public String extractDesignation(String token) { return (String) extractClaims(token).get("designation"); }
    public String extractFullName(String token) { return (String) extractClaims(token).get("fullName"); }
    public boolean isTokenExpired(String token) { return extractClaims(token).getExpiration().before(new Date()); }
}
