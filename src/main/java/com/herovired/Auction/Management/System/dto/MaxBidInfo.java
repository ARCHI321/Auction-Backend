package com.herovired.Auction.Management.System.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaxBidInfo {
    String UserId;
    String AuctionId;
    Long highestBid;
}
