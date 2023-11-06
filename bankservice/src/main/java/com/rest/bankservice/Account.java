package com.rest.bankservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private long accountNumber;
    private String accountHolder;
    private List<Transaction> transactionHistory;
    private double balance;

    public Account(long accountNumber,String accountHolder) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = 0.0;
    }
}
