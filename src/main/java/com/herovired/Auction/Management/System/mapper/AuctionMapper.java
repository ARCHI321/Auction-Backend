package com.herovired.Auction.Management.System.mapper;

import com.herovired.Auction.Management.System.dto.AuctionDto;
import com.herovired.Auction.Management.System.dto.AuctionResponse;
import com.herovired.Auction.Management.System.dto.AuctionSlotResponse;
import com.herovired.Auction.Management.System.models.Auction;


public class AuctionMapper {

    public static AuctionResponse mapToAuctionResponse(Auction auction) {
        String path = "/image/" + auction.getAuctionId();
        return AuctionResponse.builder()
                .auctionId(auction.getAuctionId())
                .title(auction.getTitle())
                .description(auction.getDescription())
                .startingPrice(auction.getStartingPrice())
                .currentPrice(auction.getCurrentPrice())
                .auctionStatus(auction.getAuctionStatus())
                .auctionType(auction.getAuctionType())
                .registerFee(auction.getRegisterFee())
                .date(auction.getSlot().getDate())
                .category(auction.getCategory())
                .sellerId(auction.getSellerId())
                .winnerId(auction.getWinnerId())
                .imageURL(path)
                .build();
    }
    public static AuctionSlotResponse mapToAuctionSlotResponse(Auction auction) {
        String path = "/image/" + auction.getAuctionId();
        return AuctionSlotResponse.builder()
                .auctionId(auction.getAuctionId())
                .title(auction.getTitle())
                .description(auction.getDescription())
                .startingPrice(auction.getStartingPrice())
                .currentPrice(auction.getCurrentPrice())
                .auctionStatus(auction.getAuctionStatus())
                .auctionType(auction.getAuctionType())
                .date(auction.getSlot().getDate())
                .category(auction.getCategory())
                .registerFee(auction.getRegisterFee())
                .sellerId(auction.getSellerId())
                .winnerId(auction.getWinnerId())
                .imageURL(path)
                .slot(auction.getSlot())
                .build();
    }

    public static AuctionDto mapToAuctionDto(Auction auction) {
        return AuctionDto.builder()
                .title(auction.getTitle())
                .description(auction.getDescription())
                .startingPrice(auction.getStartingPrice())
                .category(auction.getCategory())
                .build();
    }
}
