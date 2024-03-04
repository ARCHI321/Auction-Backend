package com.herovired.Auction.Management.System.repositories;

import com.herovired.Auction.Management.System.models.User;
import com.herovired.Auction.Management.System.models.UserAuctionRegistration;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserAuctionRegistrationRepository extends JpaRepository<UserAuctionRegistration, Long> {

    List<UserAuctionRegistration> findByAuction_AuctionId(String auctionId);

    List<UserAuctionRegistration> findByUserUserId(String userId);

    List<UserAuctionRegistration> findByAuctionAuctionId(String auctionId);

    @Modifying
    @Transactional
    @Query("DELETE FROM UserAuctionRegistration uar " +
            "WHERE uar.user.userName = :username " +
            "AND uar.auction.auctionId = :auctionId")
    void deleteByUsernameAndAuctionId(String username, String auctionId);

}

