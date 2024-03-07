package com.herovired.Auction.Management.System.controllers;

import com.herovired.Auction.Management.System.models.Transaction;
import com.herovired.Auction.Management.System.repositories.TransactionRepository;
import com.herovired.Auction.Management.System.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private TransactionRepository transactionRepository;

    @PostMapping
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        return transactionService.saveTransaction(transaction);
    }



    @GetMapping("/all-transactions")
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/all-transactions-by-user")
    public ResponseEntity<?> getAllTransactionsByUserId(@RequestParam String userName) {
        System.out.println(userName);

//        System.out.println(transactionRepository.findAll());
        var allTransactions = transactionService.getAllTransactionsByUserId(userName);
        System.out.println(allTransactions);
        return new ResponseEntity<>(allTransactions , HttpStatus.ACCEPTED);
    }

    // Other methods as needed
}

