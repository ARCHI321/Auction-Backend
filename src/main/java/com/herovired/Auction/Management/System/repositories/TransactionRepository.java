package com.herovired.Auction.Management.System.repositories;

import com.herovired.Auction.Management.System.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser_UserName(String username);
}

