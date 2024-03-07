package com.herovired.Auction.Management.System.repositories;

import com.herovired.Auction.Management.System.models.RegistrationHistory;
import com.herovired.Auction.Management.System.models.UserAuctionRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistrationHistoryRepository extends JpaRepository<RegistrationHistory, Long> {
    List<RegistrationHistory> findByAuction_AuctionId(String auctionId);

    List<RegistrationHistory> findByUserUserId(String userId);

    List<RegistrationHistory> findByAuctionAuctionId(String auctionId);
}
