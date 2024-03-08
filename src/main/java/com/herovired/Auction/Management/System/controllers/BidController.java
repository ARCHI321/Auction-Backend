package com.herovired.Auction.Management.System.controllers;

import com.herovired.Auction.Management.System.exception.CustomResponse;
import com.herovired.Auction.Management.System.models.Bid;
import com.herovired.Auction.Management.System.repositories.AuctionRepository;
import com.herovired.Auction.Management.System.repositories.BidRepository;
import com.herovired.Auction.Management.System.services.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/bid")
public class BidController {

    private final BidService bidService;

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    public BidController(BidService bidService) {
        this.bidService = bidService;
    }

    @PostMapping("/post-bids")
    public ResponseEntity<?> getBids(@RequestParam String userId, @RequestParam String auctionId ,@RequestParam Long bidAmount ) {
        Bid bid = new Bid();
        bid.setUserId(userId);
        bid.setAuctionId(auctionId);
        bid.setBidAmount(bidAmount);
        bidRepository.save(bid);

        return new ResponseEntity<>(
                new CustomResponse("Bid Posted Successfully", true), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteUserByUserId(@PathVariable String userId){
        bidRepository.deleteUserBidsByUserId(userId);
        return new ResponseEntity<>(
                new CustomResponse("All rows of that user is deleted Successfully", true), HttpStatus.OK);
    }

    @GetMapping("/get-winner")
    public ResponseEntity<?> getWinner(@RequestParam String auctionId){
        var bid = bidRepository.findMaxBidEntries();
        var auction = auctionRepository.findByAuctionId(auctionId);
        auction.setWinnerId(bid.getUserId());
        auction.setCurrentPrice(bid.getBidAmount());
        auctionRepository.save(auction);
        return new ResponseEntity<>(
                new CustomResponse(bid.getUserId() + "&" + bid.getBidAmount(), true), HttpStatus.OK);
    }

    @PutMapping("/set-winner")
    public ResponseEntity<?> setWinner(@RequestParam String auctionId , @RequestParam String userId){
        var auction = auctionRepository.findByAuctionId(auctionId);
        auction.setWinnerId(userId);
        auctionRepository.save(auction);
        return new ResponseEntity<>(
                new CustomResponse(userId , true), HttpStatus.OK);
    }

    @GetMapping("/truncate")
    public ResponseEntity<?> truncateBidsTable() {
        bidRepository.truncateBidsTable();
        return new ResponseEntity<>(
                new CustomResponse("Table truncated Successfully", true), HttpStatus.OK);
    }

    @GetMapping("/get-all-bids")
    public List<Bid> getAllBids() {
        return bidService.getAllBids();
    }
}
