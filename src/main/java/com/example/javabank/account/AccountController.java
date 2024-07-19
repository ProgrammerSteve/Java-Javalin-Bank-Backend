package com.example.javabank.account;

import com.example.javabank.utils.auth.JwtMiddleware;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import java.math.BigDecimal;

public class AccountController {
    AccountService accountService;
    JwtMiddleware jwtMiddleware;
    public AccountController(AccountService accountService, JwtMiddleware jwtMiddleware) {
        this.accountService = accountService;
        this.jwtMiddleware=jwtMiddleware;
    }
    public void registerPaths(Javalin app) {
        app.before("/view_balance",jwtMiddleware::handle);
        app.before("/withdraw",jwtMiddleware::handle);
        app.before("/deposit",jwtMiddleware::handle);

        app.post("/view_balance", this::viewBalance);
        app.post("/withdraw", this::withdraw);
        app.post("/deposit", this::deposit);
    }
    /**
     * <h3>AccountController:viewBalance:</h3>
     * Used for the <strong>"/view_balance"</strong> endpoint as a post request,
     * Grabs accountId from request body from context
     * Checks balance using accountId.
     * @apiNote Requires jwtMiddleware::handle to secure the endpoint
     * and a request body containing:
     * <ul>
     *     <li>(Integer)"accountId"</li>
     * </ul>
     *
     * @param ctx Context from Javalin
     */
    public void viewBalance(@NotNull Context ctx){
        try{
            Integer accountId=accountService.parseReqBodyForAccountId(ctx.body());
            if(accountId==null){
                ctx.status(400).result("Unable to parse req body");
                return;
            }
            ctx.status(200).result(accountService.getAccountBalance(accountId).toString());
        } catch (Exception e){
            ctx.status(400).result("Unable to get balance");
        }
    }
    /**
     * <h3>AccountController:withdraw:</h3>
     * Used for the <strong>"/withdraw"</strong> endpoint as a post request,
     * Grabs accountId and amount from request body from context
     * Checks balance using accountId to see if there's sufficient funds.
     * Will subtract the amount from the account balance if enough funds available.
     * @apiNote Requires jwtMiddleware::handle to secure the endpoint
     * and a request body containing:
     * <ul>
     *     <li>(BigDecimal)"amount"</li>
     *     <li>(Integer)"accountId"</li>
     * </ul>
     *
     * @param ctx Context from Javalin
     */
    public void withdraw(@NotNull Context ctx){
        Transaction transaction=accountService.parseReqBodyForAmountAndAccountId(ctx.body());
        if(transaction==null){
            ctx.status(400).result("Unable to parse req body");
            return;
        }
        if(!accountService.hasSufficientFunds(accountService.getAccountBalance(transaction.getAccountId()),transaction.getAmount())){
            ctx.status(400).result("insufficient funds");
            return;
        }
        BigDecimal results=accountService.withdrawFromAccount(transaction.getAmount(),transaction.getAccountId());
        if(results==null){
            ctx.status(400).result("Unable to withdraw");
            return;
        }
        ctx.status(200).result(results.toString());
    }
    /**
     * <h3>AccountController:deposit:</h3>
     * Used for the <strong>"/deposit"</strong> endpoint as a post request,
     * Grabs accountId and amount from request body from context
     * Will add the amount to the account balance.
     * @apiNote Requires jwtMiddleware::handle to secure the endpoint
     * and a request body containing:
     * <ul>
     *     <li>(BigDecimal)"amount"</li>
     *     <li>(Integer)"accountId"</li>
     * </ul>
     *
     * @param ctx Context from Javalin
     */
    public void deposit(@NotNull Context ctx){
        Transaction transaction=accountService.parseReqBodyForAmountAndAccountId(ctx.body());
        if(transaction==null){
            ctx.status(400).result("Unable to parse req body");
            return;
        }
        BigDecimal results=accountService.depositIntoAccount(transaction.getAmount(), transaction.getAccountId());
        if(results!=null){
            ctx.status(200).result(results.toString());
        }else{
            ctx.status(400).result("Unable to withdraw");
        }
    }
}
