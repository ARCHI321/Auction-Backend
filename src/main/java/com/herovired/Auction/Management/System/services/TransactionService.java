package com.herovired.Auction.Management.System.services;

import com.herovired.Auction.Management.System.models.Transaction;
import com.herovired.Auction.Management.System.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction saveTransaction(Transaction transaction) {
        // Add additional logic if needed
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getAllTransactionsByUserId(String userId) {
        return transactionRepository.findByUserUserId(userId);
    }

    // Other methods as needed
}
