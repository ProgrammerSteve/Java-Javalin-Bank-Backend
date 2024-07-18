package com.example.javabank;

import com.example.javabank.account.AccountController;
import com.example.javabank.account.AccountRepository;
import com.example.javabank.account.AccountService;
import com.example.javabank.user.UserController;
import com.example.javabank.user.UserRepository;
import com.example.javabank.user.UserService;
import com.example.javabank.utils.auth.AuthController;
import com.example.javabank.utils.auth.AuthService;
import com.example.javabank.utils.auth.JwtMiddleware;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;

public class BankController {
    public static void main(String[] args){
        Javalin app = Javalin.create(config -> {
            config.jsonMapper(new JavalinJackson());
        });
        JwtMiddleware jwtMiddleware = new JwtMiddleware();

        //Repositories
        AccountRepository accountRepository=new AccountRepository();
        UserRepository userRepository=new UserRepository(accountRepository);

        //services
        UserService userService=new UserService(userRepository);
        AccountService accountService=new AccountService(accountRepository);
        AuthService authService=new AuthService(userService);

        //Controllers
        AccountController accountController=new AccountController(accountService,userService,jwtMiddleware);
        UserController userController=new UserController(userService);
        AuthController authController=new AuthController(authService);

        //RegisterPaths
        accountController.registerPaths(app);
        userController.registerPaths(app);
        authController.registerPaths(app);

        app.start(8000);
    }
}
