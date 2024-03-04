package com.herovired.Auction.Management.System.repositories;

import com.herovired.Auction.Management.System.dto.MaxBidInfo;
import com.herovired.Auction.Management.System.models.Bid;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {

//    @Query("SELECT b.auctionId AS auctionId, b.userId AS userId, MAX(b.bidAmount) AS maxBidAmount FROM Bid b GROUP BY b.auctionId, b.userId")

    @Query("SELECT NEW com.herovired.Auction.Management.System.dto.MaxBidInfo(b.userId, b.auctionId, MAX(b.bidAmount)) FROM Bid b GROUP BY b.auctionId, b.userId")
    MaxBidInfo findMaxBidInfo();


    List<Bid> findByUserIdAndAuctionId(String userId, String auctionId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Bid b WHERE b.userId = :userId")
    void deleteUserBidsByUserId(@Param("userId") String userId);


    @Query("SELECT b FROM Bid b WHERE b.bidAmount = (SELECT MAX(bidAmount) FROM Bid)")
    Bid findMaxBidEntries();

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE auction_service.bids", nativeQuery = true)
    void truncateBidsTable();
}

