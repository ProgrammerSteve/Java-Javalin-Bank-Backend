package com.example.javabank.utils.auth;

import com.example.javabank.user.User;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import javax.security.sasl.AuthenticationException;

public class AuthController {
    AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    public void registerPaths(Javalin app) {
        app.post("/login", this::login);
    }

    private void login(Context ctx){
        String username=ctx.queryParam("username");
        String password=ctx.queryParam("password");

        try{
            User user=authService.login(username,password);
            String token = authService.generateJwtToken(user);
            ctx.json(token).status(200);
        }catch (AuthenticationException e){
            ctx.status(HttpStatus.UNAUTHORIZED);
        }
    }
}
