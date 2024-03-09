package com.herovired.Auction.Management.System.repositories;

import com.herovired.Auction.Management.System.models.Auction;
import com.herovired.Auction.Management.System.models.AuctionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;


public interface AuctionRepository extends JpaRepository<Auction, String> {
    Page<Auction> findByCategory(String category, Pageable pageable);

    Auction findByAuctionId(String auctionId);



    Page<Auction> findBySellerId(String sellerId, Pageable pageable);

    List<Auction> findByWinnerId(String winnerId);

    @Query("SELECT a FROM Auction a")
    List<Auction> findAllAuctions();

    @Query("SELECT a FROM Auction a WHERE a.auctionId = :auctionId AND a.sellerId = :sellerId")
    Auction findAuctionByAuctionIdAndSellerId(@Param("auctionId")  String auctionId , @Param("sellerId")  String sellerId);

    @Query("SELECT a FROM Auction a WHERE a.slot.date = :date")
    Page<Auction> findAllAuctionsByDate(@Param("date") LocalDate date, Pageable pageable);



//    List<Auction> findTop5ByAuctionStatusAndSlot_DateAfterOrderBySlot_DateAsc(
//            AuctionStatus auctionStatus, LocalDate currentDate);

//    @Query("SELECT a FROM Auction a " +
//            "LEFT JOIN a.slot s " +
//            "WHERE a.auctionStatus = :status " +
//            "AND s.date >= :currentDate " +
//            "ORDER BY s.date ASC")
//    List<Auction> findTop5UpcomingAuctions(@Param("status") AuctionStatus status, @Param("currentDate") LocalDate currentDate);


//    Collection<Auction> findAllByEndDateAfter(LocalDate currentDate, Sort endDate);

}
