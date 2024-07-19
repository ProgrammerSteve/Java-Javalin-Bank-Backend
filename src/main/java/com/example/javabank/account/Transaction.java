package com.example.javabank.account;

import java.math.BigDecimal;

public class Transaction {
    private Integer accountId;
    private BigDecimal amount;

    public Integer getAccountId() {
        return accountId;
    }
    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public Transaction(Integer accountId, BigDecimal amount) {
        this.accountId = accountId;
        this.amount = amount;
    }
}
