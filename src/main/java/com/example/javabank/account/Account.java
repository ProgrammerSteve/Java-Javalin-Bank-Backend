package com.example.javabank.account;

import java.math.BigDecimal;

public class Account {
    private Integer accountId;
    private Integer userId;
    private BigDecimal balance;
    private String type;

    public Account(){}

    public Account(Integer user_id, BigDecimal balance, String type) {
        this.userId = user_id;
        this.balance = balance;
        this.type=type;
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
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
