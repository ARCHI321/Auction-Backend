package com.herovired.Auction.Management.System.services;

import com.herovired.Auction.Management.System.dto.AuctionDto;
import com.herovired.Auction.Management.System.dto.AuctionResponse;
import com.herovired.Auction.Management.System.dto.AuctionSlotResponse;
import com.herovired.Auction.Management.System.dto.FrontPageDto;
import com.herovired.Auction.Management.System.exception.CustomResponse;
import com.herovired.Auction.Management.System.exception.InvalidSlotNumberException;
import com.herovired.Auction.Management.System.exception.AuctionClosedForUpdateException;

import com.herovired.Auction.Management.System.models.Auction;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IAuctionService {
    AuctionResponse createAuction(AuctionDto auctionDto) throws Exception, InvalidSlotNumberException;

    AuctionDto updateAuction(AuctionDto auctionDto, String auctionId) ;

    void deleteAuction(String auctionId);

    Page<AuctionSlotResponse> getAllAuction(int page);

    Page<AuctionSlotResponse> getAllAuctionByDate(int page);

    AuctionResponse getAuctionById(String auctionId);

    Page<AuctionResponse> getAuctionByCategory(String category, int page);

    Page<AuctionResponse> getAuctionBySellerId(String sellerId, int page);

    Page<AuctionSlotResponse> getAuctionResponseBySellerId(String sellerId, int page);

    List<AuctionResponse> getAuctionByWinnerId(String winnerId);

    byte[] downloadImage(String fileName);

    public AuctionSlotResponse getAuctionResponseById(String auctionId);

    public List<FrontPageDto> getTop5UpcomingAuctions();

    public CustomResponse checkWinnerAndPaymentStatus(String auctionId);
}
