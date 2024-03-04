package com.herovired.Auction.Management.System.dto;

import com.herovired.Auction.Management.System.models.AuctionStatus;
import com.herovired.Auction.Management.System.models.AuctionType;
import com.herovired.Auction.Management.System.models.Slot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;





@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuctionSlotResponse {
    private String auctionId;
    private String title;
    private String description;
    private long startingPrice;
    private Long currentPrice;
    private AuctionStatus auctionStatus;
    private AuctionType auctionType;
    private String category;
    private LocalDate date;
    private String sellerId;
    private String winnerId;
    private String imageURL;
    private Long registerFee;
    private Slot slot;

}

