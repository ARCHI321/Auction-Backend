package com.herovired.Auction.Management.System.repositories;

import com.herovired.Auction.Management.System.models.Transaction;
import com.herovired.Auction.Management.System.models.UserAuctionRegistration;
import com.herovired.Auction.Management.System.util.TransactionStatus;
import com.herovired.Auction.Management.System.util.TransactionType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {


    List<Transaction> findByUserUserId(String userId);




    @Query("SELECT t.status FROM Transaction t " +
            "WHERE t.transactionId = :transactionId")
    String findByTransactionId(String transactionId);

    @Transactional
    @Modifying
    @Query("UPDATE Transaction t SET t.status = :status WHERE t.transactionId = :transactionId")
    void updateStatusByTransactionId(
            @Param("transactionId") String transactionId,
            @Param("status") TransactionStatus status);


    Optional<Transaction> findByUser_UserNameAndAuction_AuctionIdAndType(String userName, String auctionId, TransactionType type);

}

