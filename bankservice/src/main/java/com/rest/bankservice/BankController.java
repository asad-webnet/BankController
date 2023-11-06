package com.rest.bankservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
public class BankController {

    private Map<Integer, Account> accountMap = new HashMap<>();
    private long transactionId = 0;

    @PostMapping
    public ResponseEntity<?> createAccount(@RequestParam int accountNumber, @RequestParam String accountHolder) {
        if (!accountMap.containsKey(accountNumber)) {
            Account newAccount = new Account(accountNumber, accountHolder);
            accountMap.put(accountNumber, newAccount);
            return new ResponseEntity<>(newAccount, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Account with this number already exists", HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestParam int accountNumber, @RequestParam double amount) {
        Account account = accountMap.get(accountNumber);
        if (account != null) {
            Transaction transaction = new Transaction();
            transaction.setTransactionId(++transactionId + "");
            transaction.setType(TransactionType.DEPOSIT);
            transaction.setAmount(amount);

            account.getTransactionHistory().add(transaction);
            account.setBalance(account.getBalance()+amount);

            return new ResponseEntity<>(account, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestParam int accountNumber, @RequestParam double amount) {
        Account account = accountMap.get(accountNumber);
        if (account != null) {
            if (account.getBalance() >= amount) {
                Transaction transaction = new Transaction();
                transaction.setTransactionId(++transactionId + "");
                transaction.setType(TransactionType.WITHDRAWAL);
                transaction.setAmount(amount);

                // Updating account balance and history
                account.getTransactionHistory().add(transaction);
                account.setBalance(account.getBalance() - amount);

                return new ResponseEntity<>(account, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Insufficient balance", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping
    public ResponseEntity<?> getAllAccounts() {
        var accounts = accountMap.values();
        if (accounts.size() != 0) {
            return new ResponseEntity<>(accounts, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> getAccount(@PathVariable int accountNumber) {
        Account account = accountMap.get(accountNumber);
        if (account != null) {
            return new ResponseEntity<>(account, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<?> removeAccount(@PathVariable int accountNumber) {
        if (accountMap.containsKey(accountNumber)) {
            accountMap.remove(accountNumber);
            return new ResponseEntity<>("Account removed successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
        }
    }
}

