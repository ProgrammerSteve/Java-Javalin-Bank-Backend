package com.example.javabank.account;

import java.math.BigDecimal;

public class Account {
    private Integer accountId;
    private Integer userId;
    private BigDecimal balance;

    public Account(){}

    public Account(Integer user_id, BigDecimal balance) {
        this.userId = user_id;
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer user_id) {
        this.userId = user_id;
    }
    public Integer getAccountId() {
        return accountId;
    }
    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }
}
