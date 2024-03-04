package com.herovired.Auction.Management.System.dto;

import com.herovired.Auction.Management.System.models.AuctionStatus;
import com.herovired.Auction.Management.System.models.AuctionType;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class AuctionResponse {
    private String auctionId;
    private String title;
    private String description;
    private long startingPrice;
    private Long currentPrice;
    private AuctionStatus auctionStatus;
    private AuctionType auctionType;
    private String category;
    private Long registerFee;
    private LocalDate date;
    private String sellerId;
    private String winnerId;
    private String imageURL;

}
