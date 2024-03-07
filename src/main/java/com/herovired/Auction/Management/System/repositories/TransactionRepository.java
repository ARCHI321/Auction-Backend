package com.herovired.Auction.Management.System.repositories;

import com.herovired.Auction.Management.System.models.Transaction;
import com.herovired.Auction.Management.System.util.TransactionStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {


    @Query("SELECT t.status FROM Transaction t " +
            "WHERE t.user.userName = :userName")
    String findByUser_UserName(String userName);




    @Query("SELECT t.status FROM Transaction t " +
            "WHERE t.transactionId = :transactionId")
    String findByTransactionId(String transactionId);

    @Transactional
    @Modifying
    @Query("UPDATE Transaction t SET t.status = :status WHERE t.transactionId = :transactionId")
    void updateStatusByTransactionId(
            @Param("transactionId") String transactionId,
            @Param("status") TransactionStatus status);

}

