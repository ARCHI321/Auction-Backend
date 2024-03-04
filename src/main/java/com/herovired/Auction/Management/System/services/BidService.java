package com.herovired.Auction.Management.System.services;

import com.herovired.Auction.Management.System.dto.MaxBidInfo;
import com.herovired.Auction.Management.System.models.Bid;
import com.herovired.Auction.Management.System.repositories.BidRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BidService {
    private final BidRepository bidRepository;

    @Autowired
    public BidService(BidRepository bidRepository) {
        this.bidRepository = bidRepository;
    }

    public Bid saveBid(Bid bid) {
        return bidRepository.save(bid);
    }

    @Transactional
    public void truncateBidTable() {
        bidRepository.deleteAllInBatch();
    }

    public MaxBidInfo findMaxBidInfo() {
        System.out.println(bidRepository.findMaxBidInfo());
        return bidRepository.findMaxBidInfo();
    }

    public List<Bid> getBidsByUserIdAndAuctionId(String userId, String auctionId) {
        return bidRepository.findByUserIdAndAuctionId(userId, auctionId);
    }



    public List<Bid> getAllBids() {
        return bidRepository.findAll();
    }


}

