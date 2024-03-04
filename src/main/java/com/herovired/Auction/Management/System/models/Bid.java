package com.herovired.Auction.Management.System.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "bids")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "auction_id")
    private String auctionId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "bid_amount")
    private Long bidAmount;

    // Constructors, getters, setters, etc.
}

