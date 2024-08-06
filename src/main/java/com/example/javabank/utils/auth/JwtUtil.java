package com.example.javabank.utils.auth;


import com.example.javabank.user.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;


public class JwtUtil {
    private static final long EXPIRATION_TIME = 86400000; // 1 day in milliseconds

    public static String generateToken(User user) {
        String secretKey = System.getProperty("secret.key");
        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalStateException("Database environment variables are missing");
        }
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId", user.getUserId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public static Claims validateToken(String token) {
        String secretKey = System.getProperty("secret.key");
        if (secretKey == null) {
            throw new IllegalStateException("Database environment variables are missing");
        }
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}