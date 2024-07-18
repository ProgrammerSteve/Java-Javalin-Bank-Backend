package com.example.javabank.user;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.List;

public class UserController {
    UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    public void registerPaths(Javalin app) {
        app.get("/getAll", this::getAllUsers);
        app.post("/register/{accountType}", this::register);
    }
    public void getAllUsers(Context ctx){
        List<User> users=userService.findAllUsers();
        ctx.status(200);
        ctx.result(users.toString());
    }

    public void register(Context ctx){
        User user=ctx.bodyAsClass(User.class);
        String accountType=ctx.pathParam("accountType");

        boolean isSuccessful=userService.createUser(user,accountType);
        if(isSuccessful){
            ctx.status(200);
            ctx.result("account registered!");
        }else{
            ctx.status(401);
            ctx.result("Error creating user with request body");
        }
    }
}
