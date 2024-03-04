package com.herovired.Auction.Management.System.services;

import com.herovired.Auction.Management.System.controllers.WebSocketController;
import com.herovired.Auction.Management.System.controllers.WebSocketController1;
import com.herovired.Auction.Management.System.models.Auction;
import com.herovired.Auction.Management.System.models.AuctionStatus;
import com.herovired.Auction.Management.System.models.Bid;
import com.herovired.Auction.Management.System.models.User;
import com.herovired.Auction.Management.System.repositories.AuctionRepository;
import com.herovired.Auction.Management.System.repositories.BidRepository;
import com.herovired.Auction.Management.System.repositories.UserAuctionRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Service
public class AuctionService {

    @Autowired
    private UserAuctionRegistrationRepository userAuctionRegistrationRepository;

    @Autowired
    private UserAuctionRegistrationService userAuctionRegistrationService;
    @Autowired
    private BidService bidService;

    @Autowired
    private BidRepository bidRepository;

    private int activeFlag = 0;

    private int announcedFlag = 0;
    private Auction activeAuction;
    private List<String> registeredUserNames;
    private List<String> registeredNames;


    private final AuctionRepository auctionRepository;

    public AuctionService(AuctionRepository auctionRepository) {
        this.auctionRepository = auctionRepository;
    }

    public void updateAuctionStatuses() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        LocalDateTime currentDateTime1 = LocalDateTime.now();


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");


        String formattedDateTime = currentDateTime1.format(formatter);


        LocalDateTime currentDateTime = LocalDateTime.parse(formattedDateTime, formatter);


        List<Auction> allAuctions = auctionRepository.findAllAuctions();

        allAuctions.forEach(auction -> {
            //System.out.println("BEFORE QUEUED " + "ActiveFlag = " + activeFlag + "Announced Flag = " + announcedFlag);


            LocalDate auctionDate = auction.getSlot().getDate();
            LocalTime thirtyMinutesBeforeStartTime = auction.getSlot().getStartTime().minusMinutes(30);
            LocalTime StartTime = auction.getSlot().getStartTime();

            LocalDateTime dateTimeForQueuedStatus = auctionDate.atTime(thirtyMinutesBeforeStartTime);
            LocalDateTime dateTimeForQueuedStatus2 = auctionDate.atTime(StartTime);



            if (( currentDateTime.isAfter(dateTimeForQueuedStatus) || currentDateTime.isEqual(dateTimeForQueuedStatus) ) && currentDateTime.isBefore(dateTimeForQueuedStatus2)) {
                auction.setAuctionStatus(AuctionStatus.QUEUE);
                auctionRepository.save(auction);
                //activeFlag = 1;
            }

            //System.out.println("AFTER QUEUED " + "ActiveFlag = " + activeFlag + "Announced Flag = " + announcedFlag);
        });

        // Update status to "ACTIVE" for auctions whose time has come
        allAuctions.forEach(auction -> {
            //System.out.println("BEFORE ACTIVE " + "ActiveFlag = " + activeFlag + "Announced Flag = " + announcedFlag);

            LocalDate auctionDate = auction.getSlot().getDate();

            LocalTime EndTime = auction.getSlot().getEndTime();
            LocalTime StartTime = auction.getSlot().getStartTime();

            LocalDateTime dateTimeForQueuedStatus2 = auctionDate.atTime(EndTime);
            LocalDateTime dateTimeForQueuedStatus = auctionDate.atTime(StartTime);



            if ((currentDateTime.isAfter(dateTimeForQueuedStatus)  || currentDateTime.isEqual(dateTimeForQueuedStatus) ) && (currentDateTime.isBefore(dateTimeForQueuedStatus2) || currentDateTime.isEqual(dateTimeForQueuedStatus)) ) {
                auction.setAuctionStatus(AuctionStatus.ACTIVE);
                auctionRepository.save(auction);
                System.out.println(auction.getAuctionId());
                var allUserRegisteredForParticularAuction = userAuctionRegistrationService.findUsersByAuctionId(auction.getAuctionId());
                System.out.println(allUserRegisteredForParticularAuction);

                activeAuction = auction;
                List<String> userNames = new ArrayList<>();
                List<String> names = new ArrayList<>();
                for(User user:allUserRegisteredForParticularAuction){
                    userNames.add(user.getUserName());
                    names.add(user.getName());
                }
                WebSocketController1.sendBroadcastMessage("Auction " +auction.getTitle() + " is now active @@@@: " + auction.getAuctionId() + "##### registered by " + names + "actual: !!!![" + userNames  + "]!!!!");
                registeredUserNames = userNames;
                registeredNames = names;
                announcedFlag = 1;


            }

            //System.out.println("AFTER ACTIVE " + "ActiveFlag = " + activeFlag + "Announced Flag = " + announcedFlag);

        });

        scheduler.scheduleAtFixedRate(() -> {
            //WebSocketController1.sendBroadcastMessage("Auction is still active: " + auction.getAuctionId() + " registered by " + allUserRegisteredForParticularAuction);
            WebSocketController1.sendBroadcastMessage("Auction " +activeAuction.getTitle() + " is still active @@@@: " + activeAuction.getAuctionId() + "##### registered by " + registeredNames + "actual: !!!![" + registeredUserNames  + "]!!!!");

        }, 0, 1, TimeUnit.MINUTES);

        // Schedule a task to stop sending messages after 5 minutes
        scheduler.schedule(() -> {
            scheduler.shutdown();
        }, 5, TimeUnit.MINUTES);

        // Update status to "CLOSED" for closed auctions
        allAuctions.forEach(auction -> {
            //System.out.println("BEFORE CLOSED " + "ActiveFlag = " + activeFlag + "Announced Flag = " + announcedFlag);

            //System.out.println("For Closed auctions");
            LocalDate auctionDate = auction.getSlot().getDate();
            LocalTime thirtyMinutesBeforeEndTime = auction.getSlot().getEndTime().minusMinutes(30);
            LocalTime EndTime = auction.getSlot().getEndTime();

            LocalDateTime dateTimeForQueuedStatus = auctionDate.atTime(thirtyMinutesBeforeEndTime);
            LocalDateTime dateTimeForQueuedStatus2 = auctionDate.atTime(EndTime);

            if (currentDateTime.isAfter(dateTimeForQueuedStatus2)) {
                //activeFlag = 0;
                //announcedFlag = 0;
                auction.setAuctionStatus(AuctionStatus.CLOSED);
                auctionRepository.save(auction);

//                var allBids = bidRepository.findAll();
//                WebSocketController.sendBroadcastMessage("Bid History: " + allBids);
//
//                var highestBidInfo = bidService.findMaxBidInfo();
//                System.out.println((highestBidInfo));
//                var auctionSold = auctionRepository.findAuctionByAuctionIdAndSellerId(highestBidInfo.getAuctionId(),highestBidInfo.getUserId());
//
//                WebSocketController.sendBroadcastMessage("Winner is : " + allBids);
//                auctionSold.setWinnerId(highestBidInfo.getUserId());
//                auctionSold.setCurrentPrice(highestBidInfo.getHighestBid());


//                try {
//                    Thread.sleep(3000); // Simulate a 3-second delay
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                }
//
//                bidService.truncateBidTable();



            }
            //System.out.println(" AFTER CLOSED " + "ActiveFlag = " + activeFlag + "Announced Flag = " + announcedFlag);


        });
    }
}
