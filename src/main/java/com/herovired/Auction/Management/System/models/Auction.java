package com.herovired.Auction.Management.System.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.herovired.Auction.Management.System.util.ValidCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;


@Entity
@Table(name = "auction")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Auction {

    @Id
    @Column(name = "auction_id", updatable = false)
    private String auctionId;

    @NotBlank(message = "Title cannot be empty")
    @Column(name = "title")
    private String title;

    @NotBlank(message = "Description cannot be empty")
    @Column(name = "description")
    private String description;

    @NotNull(message = "Starting price is required")
    @Positive(message = "Starting price must be positive")
    @Column(name = "starting_price")
    @Min(value = 1000, message = "Starting price must be minimum price 1000")
    private Long startingPrice;

    @NotNull(message = "Current price is required")
    @Positive(message = "Current price must be positive")
    @Column(name = "current_price")
    private Long currentPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "auctionStatus")
    private AuctionStatus auctionStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "auctionType")
    private AuctionType auctionType;

    @Column(name = "category")
    @ValidCategory
    private String category;


    @Column(name = "register_fee")
    private Long registerFee;

    @Column(name = "seller_id")
    private String sellerId;

    @Column(name = "winner_id")
    private String winnerId;

    @OneToOne(cascade = CascadeType.ALL)
    private Slot slot;

    @OneToOne(cascade = CascadeType.ALL)
    private Images images;

    @OneToMany(mappedBy = "auction" , cascade = CascadeType.ALL)
    private Set<UserAuctionRegistration> registrations;

    @OneToMany(mappedBy = "auction", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

}
