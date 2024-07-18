package com.example.javabank.account;

import com.example.javabank.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.Optional;

public class AccountService {
    private final AccountRepository accountRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    public BigDecimal getAccountBalance(User user){
        Optional<BigDecimal> balanceOptional=accountRepository.getBalance(user.getUserId());
        if(balanceOptional.isPresent()){
            return balanceOptional.get();
        }
        throw new RuntimeException("Unable to get balance using userId...");
    }



    public BigDecimal depositIntoAccount(User user, BigDecimal amount){
        Optional<BigDecimal> balanceOptional=accountRepository.getBalance(user.getUserId());
        if(balanceOptional.isPresent()){
            BigDecimal balance=balanceOptional.get();
            BigDecimal newBalance=amount.add(balance);
            accountRepository.updateBalance(user.getUserId(),newBalance);
            return newBalance;
        }else{
            return null;
        }
    }

    public BigDecimal withdrawFromAccount(User user, BigDecimal amount){
        Optional<BigDecimal> balanceOptional=accountRepository.getBalance(user.getUserId());
        if(balanceOptional.isPresent()){
            BigDecimal balance=balanceOptional.get();
            BigDecimal newBalance=balance.subtract(amount);
            accountRepository.updateBalance(user.getUserId(),newBalance);
            return newBalance;
        }else{
            return null;
        }
    }

    public boolean hasSufficientFunds(BigDecimal balance, BigDecimal withdraw){
        return (balance.compareTo(withdraw)>=0);
    }

    public BigDecimal parseReqBodyForAmount(String reqBody){
        BigDecimal amount;
        try {
            Map<String, Object> requestBodyMap = objectMapper.readValue(reqBody, Map.class);
            Object amountObj = requestBodyMap.get("amount");
            if (amountObj != null) {
                String amountStr = amountObj.toString(); // Convert to String
                double amountDbl = Double.parseDouble(amountStr); // Convert to double
                amount=BigDecimal.valueOf(amountDbl);// Convert to BigDecimal
            } else {
                amount=null;
            }
        } catch (IOException e) {
            return null;
        }
        return amount;
    }
}
