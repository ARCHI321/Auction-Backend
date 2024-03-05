package com.herovired.Auction.Management.System.controllers;

import com.herovired.Auction.Management.System.models.Transaction;
import com.herovired.Auction.Management.System.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        return transactionService.saveTransaction(transaction);
    }

    @GetMapping("/all-transactions")
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/all-transactions-by-user")
    public List<Transaction> getAllTransactionsByUserId(@RequestParam String userId) {
        return transactionService.getAllTransactionsByUserId(userId);
    }

    // Other methods as needed
}

