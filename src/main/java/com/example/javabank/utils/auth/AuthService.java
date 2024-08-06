package com.example.javabank.utils.auth;

import com.example.javabank.user.User;
import com.example.javabank.user.UserService;

import javax.security.sasl.AuthenticationException;

public class AuthService {

    UserService userService;

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    public User login(String username, String password) throws AuthenticationException {
        User user=userService.findByUsername(username);
        if(user == null) throw new AuthenticationException("Invalid user credentials, please try again");
        String storedPassword=user.getPassword();
        boolean isVerified=PasswordHasher.verifyPassword(password,storedPassword);
        if(!isVerified) throw new AuthenticationException("Invalid user credentials, please try again");
        return user;
    }

    public String generateJwtToken(User user) {
        return JwtUtil.generateToken(user);
    }



}
