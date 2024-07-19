package com.example.javabank.account;

import com.example.javabank.utils.database.DatabaseConfig;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AccountRepository {
    DataSource dataSource= DatabaseConfig.createDataSource();
    private String sqlInsertAccount = "INSERT INTO accounts (balance, user_id, account_type) VALUES (?, ?, ?)";
    private String sqlViewBalance = "SELECT balance FROM accounts WHERE user_id=? AND account_id=?";
    private String sqlUpdateBalance = "UPDATE accounts SET balance = ? WHERE user_id = ? AND account_id=?";

    public boolean createAccount(Account account){
        try(Connection connection= dataSource.getConnection()){
            PreparedStatement insertPs=connection.prepareStatement(sqlInsertAccount);
            insertPs.setBigDecimal(1,account.getBalance());
            insertPs.setInt(2,account.getUserId());
            insertPs.setString(3,account.getType());
            int insertCount=insertPs.executeUpdate();
            return insertCount>0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    public boolean updateBalance(Integer userId, BigDecimal newBalance, Integer accountId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlUpdateBalance)
        ) {
            ps.setBigDecimal(1, newBalance);
            ps.setInt(2, userId);
            ps.setInt(3,accountId);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public Optional<BigDecimal> getBalance(Integer userId, Integer accountId) {
        try (Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sqlViewBalance)) {
            ps.setInt(1, userId);
            ps.setInt(2,accountId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                BigDecimal balance = rs.getBigDecimal("balance");
                return Optional.of(balance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
