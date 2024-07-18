package com.example.javabank.account;

import com.example.javabank.user.User;
import com.example.javabank.user.UserService;
import com.example.javabank.utils.auth.JwtMiddleware;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.jsonwebtoken.Claims;

import java.math.BigDecimal;


public class AccountController {
    AccountService accountService;
    UserService userService;
    JwtMiddleware jwtMiddleware;

    public AccountController(AccountService accountService, UserService userService, JwtMiddleware jwtMiddleware) {
        this.accountService = accountService;
        this.userService=userService;
        this.jwtMiddleware=jwtMiddleware;
    }

    public void registerPaths(Javalin app) {
        app.before("/view_balance",jwtMiddleware::handle);
        app.before("/withdraw",jwtMiddleware::handle);
        app.before("/deposit",jwtMiddleware::handle);
        app.get("/view_balance", this::viewBalance);
        app.post("/withdraw", this::withdraw);
        app.post("/deposit", this::deposit);
    }

    public void viewBalance(Context ctx){
        try{
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
            User user=userService.findByUserId(userId);
            BigDecimal amount=accountService.getAccountBalance(user);
            ctx.status(200);
            ctx.result(amount.toString());
        }catch (NullPointerException e){
            ctx.status(400);
            ctx.result("Unable to retrieve UserId from jwt");
        } catch (Exception e){
            ctx.status(400);
            ctx.result("Unable to get balance");
        }
    }

    public void withdraw(Context ctx){
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
        User user=userService.findByUserId(userId);
        BigDecimal amount=accountService.parseReqBodyForAmount(ctx.body());
        if(amount==null){
            ctx.status(400);
            ctx.result("Unable to parse req body");
            return;
        }
        BigDecimal balance=accountService.getAccountBalance(user);
        if(!accountService.hasSufficientFunds(balance,amount)){
            ctx.status(400);
            ctx.result("insufficient funds");
            return;
        }
        BigDecimal results=accountService.withdrawFromAccount(user,amount);
        if(results==null){
            ctx.status(400);
            ctx.result("Unable to withdraw");
            return;
        }
        ctx.status(200);
        ctx.result(results.toString());
    }

    public void deposit(Context ctx){
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
        User user=userService.findByUserId(userId);
        BigDecimal amount=accountService.parseReqBodyForAmount(ctx.body());
        if(amount==null){
            ctx.status(400);
            ctx.result("Unable to parse req body");
            return;
        }
        BigDecimal results=accountService.depositIntoAccount(user,amount);
        if(results!=null){
            ctx.status(200);
            ctx.result(results.toString());
        }else{
            ctx.status(400);
            ctx.result("Unable to withdraw");
        }
    }
}
