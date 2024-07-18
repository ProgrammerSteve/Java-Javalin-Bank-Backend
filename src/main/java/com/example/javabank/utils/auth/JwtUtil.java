package com.example.javabank.utils.auth;


import com.example.javabank.user.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

public class JwtUtil {
    private static final String PROPERTIES_FILE = "application.properties";
    private static final long EXPIRATION_TIME = 86400000; // 1 day in milliseconds
    private static String secretKey;

    static {
        Properties properties = new Properties();
        try (InputStream input = JwtUtil.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                throw new IllegalStateException("Sorry, unable to find " + PROPERTIES_FILE);
            }
            properties.load(input);
            secretKey = properties.getProperty("secret.key");
            if (secretKey == null) {
                throw new IllegalStateException("Secret key not found in properties file");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load properties file", e);
        }
    }

    public static String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId", user.getUserId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public static Claims validateToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}