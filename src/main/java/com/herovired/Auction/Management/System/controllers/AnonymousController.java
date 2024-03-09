package com.herovired.Auction.Management.System.controllers;

import com.herovired.Auction.Management.System.dto.FrontPageDto;
import com.herovired.Auction.Management.System.models.Auction;
import com.herovired.Auction.Management.System.services.IAuctionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/anonymous")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AnonymousController {
    private IAuctionService auctionService;


    @GetMapping("/upcoming-auctions")
    public ResponseEntity<List<FrontPageDto>> getTop5UpcomingAuctions(){
        List<FrontPageDto> upcomingAuctions = auctionService.getTop5UpcomingAuctions();
        return new ResponseEntity<>(upcomingAuctions, HttpStatus.OK);
    }

}
