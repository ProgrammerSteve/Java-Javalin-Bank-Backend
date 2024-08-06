package com.example.javabank.user;

import com.example.javabank.account.Account;
import com.example.javabank.utils.auth.JwtMiddleware;
import com.example.javabank.utils.auth.PasswordHasher;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.jsonwebtoken.Claims;

import java.util.List;

public class UserController {
    UserService userService;
    JwtMiddleware jwtMiddleware;

    public UserController(UserService userService, JwtMiddleware jwtMiddleware) {
        this.userService = userService;
        this.jwtMiddleware=jwtMiddleware;
    }

    public void registerPaths(Javalin app) {
        app.before("/get_accounts_by_user_id",jwtMiddleware::handle);
        app.get("/get_accounts_by_user_id",this::getAccountsByUserId);
        app.get("/get_all_users", this::getAllUsers);
        app.post("/register/{accountType}", this::register);
    }
    public void getAccountsByUserId(Context ctx){
        Claims claims=ctx.attribute("claims");
        if(claims==null){
            ctx.status(400);
            ctx.result("Unable to get jwt payload");
            return;
        }
        Integer userId = claims.get("userId", Integer.class);
        if(userId==null){
            ctx.status(400);
            ctx.result("Unable to get userId from jwt payload");
            return;
        }
        List<Account> accounts=userService.getAccountsByUserId(userId);
        System.out.println(accounts.toString());
        ctx.json(accounts).status(200);
    }




    public void getAllUsers(Context ctx){
        List<User> users=userService.findAllUsers();
        ctx.status(200);
        ctx.result(users.toString());
    }

    public void register(Context ctx){
        User user=ctx.bodyAsClass(User.class);
        String accountType=ctx.pathParam("accountType");

        String hashedPassword = PasswordHasher.hashPasswordWithSalt(user.getPassword());
        user.setPassword(hashedPassword);

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
