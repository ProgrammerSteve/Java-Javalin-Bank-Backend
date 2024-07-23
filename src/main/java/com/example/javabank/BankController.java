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
import io.github.cdimascio.dotenv.Dotenv;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;

public class BankController {
    public static void main(String[] args){

        try{
            Dotenv dotenv = Dotenv.load();

            String secretKey = dotenv.get("SECRET_KEY");
            String jdbcUrl = dotenv.get("JDBC_URL");
            String jdbcUsername = dotenv.get("JDBC_USERNAME");
            String jdbcPassword = dotenv.get("JDBC_PASSWORD");

            if (secretKey == null) {
                throw new IllegalStateException("secretkey environment variables are missing");
            }

            if (jdbcUrl == null || jdbcUsername == null || jdbcPassword == null) {
                throw new IllegalStateException("database environment variables are missing");
            }

            System.setProperty("secret.key", secretKey);
            System.setProperty("jdbc.url", jdbcUrl);
            System.setProperty("jdbc.username", jdbcUsername);
            System.setProperty("jdbc.password", jdbcPassword);

        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Failed to load properties file", e);
        }

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
        AccountController accountController=new AccountController(accountService,jwtMiddleware);
        UserController userController=new UserController(userService,jwtMiddleware);
        AuthController authController=new AuthController(authService);

        //RegisterPaths
        accountController.registerPaths(app);
        userController.registerPaths(app);
        authController.registerPaths(app);

        app.start(8000);
    }
}
