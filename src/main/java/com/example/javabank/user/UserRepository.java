package com.example.javabank.user;

import com.example.javabank.account.Account;
import com.example.javabank.account.AccountRepository;
import com.example.javabank.account.AccountService;
import com.example.javabank.utils.database.DatabaseConfig;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository {
    private String sqlFindByUsername="select * from USERS where username=?";
    private String sqlFindByUserId="select * from USERS where user_id=?";
    private String sqlFindAllUsers="select * from USERS";
    private String sqlCreateUser="insert into USERS (username, password) values (?, ?) RETURNING user_id";
    private String sqlFindAccountsByUserId="SELECT account_id, balance, account_type FROM accounts INNER JOIN users ON users.user_id=accounts.user_id WHERE users.user_id = ?";
    DataSource dataSource= DatabaseConfig.createDataSource();


    AccountRepository accountRepository;
    public UserRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountRepository getAccountRepository() {
        return accountRepository;
    }

    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> getAccountsByUserId(Integer userId){
        List<Account>results=new ArrayList<>();
        try(Connection connection= dataSource.getConnection()){
            PreparedStatement ps=connection.prepareStatement(sqlFindAccountsByUserId);
            ps.setInt(1,userId);
            ResultSet resultSet=ps.executeQuery();
            while(resultSet.next()){
                BigDecimal balance=resultSet.getBigDecimal("balance");
                String type=resultSet.getString("account_type");
                Integer accountId=resultSet.getInt("account_id");
                Account pulledAccount=new Account(accountId,userId,balance,type);
                System.out.println("pulled: "+pulledAccount.toString());
                results.add(pulledAccount);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return results;
    }

    public List<User> findAllUsers(){
        List<User>results=new ArrayList<>();
        try(Connection connection= dataSource.getConnection()){
            PreparedStatement ps=connection.prepareStatement(sqlFindAllUsers);
            ResultSet resultSet=ps.executeQuery();
            while(resultSet.next()){
                String name=resultSet.getString("username");
                String password=resultSet.getString("password");
                Integer userId=resultSet.getInt("user_id");
                User pulledUser=new User(name,password);
                pulledUser.setUserId(userId);
                System.out.println("pulled: "+pulledUser.toString());
                results.add(pulledUser);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return results;
    }
    public Optional<User> findByUsername(String username){
        try(Connection connection= dataSource.getConnection()){
            PreparedStatement ps=connection.prepareStatement(sqlFindByUsername);
            ps.setString(1,username);
            ResultSet resultSet=ps.executeQuery();
            if(!resultSet.next()){
                return Optional.empty();
            }
            User foundUser=new User(
                    resultSet.getString("username"),
                    resultSet.getString("password")
            );
            foundUser.setUserId(resultSet.getInt("user_id"));
            return Optional.of(foundUser);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }
    public Optional<User> findByUserId(Integer userId){
        try(Connection connection= dataSource.getConnection()){
            PreparedStatement ps=connection.prepareStatement(sqlFindByUserId);
            ps.setInt(1,userId);
            ResultSet resultSet=ps.executeQuery();
            if(!resultSet.next()){
                return Optional.empty();
            }
            User foundUser=new User(
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    userId
            );
            return Optional.of(foundUser);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean createUser(User user,String accountType){
        try(Connection connection= dataSource.getConnection()){
            Integer userId = null;

            try (PreparedStatement insertUserPs = connection.prepareStatement(sqlCreateUser)) {
                insertUserPs.setString(1, user.getUsername());
                insertUserPs.setString(2, user.getPassword());

                ResultSet rs = insertUserPs.executeQuery();
                if (rs.next()) {
                    userId = rs.getInt("user_id");
                }else{
                    new RuntimeException("user_id was not retrieved from create User");
                }
                Account newAccount=new Account(userId, new BigDecimal(0),accountType);
                return accountRepository.createAccount(newAccount);
            }catch(SQLException e){
                e.printStackTrace();
            }
            return false;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
