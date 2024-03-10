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

import java.time.Duration;
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

    @Autowired
    private EmailService emailService;

    private int activeFlag = 0;

    private int announcedFlag = 0;
    private LocalDateTime lastAnnouncementTime;
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



            LocalDate auctionDate = auction.getSlot().getDate();
            LocalTime thirtyMinutesBeforeStartTime = auction.getSlot().getStartTime().minusMinutes(30);
            LocalTime StartTime = auction.getSlot().getStartTime();

            LocalDateTime dateTimeForQueuedStatus = auctionDate.atTime(thirtyMinutesBeforeStartTime);
            LocalDateTime dateTimeForQueuedStatus2 = auctionDate.atTime(StartTime);



            if (( currentDateTime.isAfter(dateTimeForQueuedStatus) || currentDateTime.isEqual(dateTimeForQueuedStatus) ) && currentDateTime.isBefore(dateTimeForQueuedStatus2)) {

                auction.setAuctionStatus(AuctionStatus.QUEUE);
                auctionRepository.save(auction);

            }

        });

        // Update status to "ACTIVE" for auctions whose time has come
        allAuctions.forEach(auction -> {


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
                List<String> email = new ArrayList<>();
                for(User user:allUserRegisteredForParticularAuction){
                    userNames.add(user.getUserName());
                    names.add(user.getName());
                    email.add(user.getEmail());
                }
                registeredUserNames = userNames;
                registeredNames = names;

                WebSocketController1.sendBroadcastMessage("Auction " +auction.getTitle() + " is now active @@@@: " + auction.getAuctionId() + "##### registered by " + names + "actual: !!!![" + userNames  + "]!!!!");

                scheduler.scheduleAtFixedRate(() -> {
                    WebSocketController1.sendBroadcastMessage("Auction " +activeAuction.getTitle() + " is still active @@@@: " + activeAuction.getAuctionId() + "##### registered by " + registeredNames + "actual: !!!![" + registeredUserNames  + "]!!!!");

                }, 0, 1, TimeUnit.MINUTES);

                // Schedule a task to stop sending messages after 5 minutes
                scheduler.schedule(() -> {
                    scheduler.shutdown();
                }, 5, TimeUnit.MINUTES);


                if (announcedFlag < 2) {
                    for (String e : email) {
                        // Assuming emailService is an instance of your EmailService class
                        emailService.sendEmail(e, "Auction " + auction.getTitle() + " is now active", "Auction " + auction.getTitle() + " is now active");
                    }
                    announcedFlag++;

                    WebSocketController1.sendBroadcastMessage("Auction " +auction.getTitle() + " is now active @@@@: " + auction.getAuctionId() + "##### registered by " + names + "actual: !!!![" + userNames  + "]!!!!");

                    scheduler.scheduleAtFixedRate(() -> {
                        WebSocketController1.sendBroadcastMessage("Auction " +activeAuction.getTitle() + " is still active @@@@: " + activeAuction.getAuctionId() + "##### registered by " + registeredNames + "actual: !!!![" + registeredUserNames  + "]!!!!");

                    }, 0, 1, TimeUnit.MINUTES);

                    // Schedule a task to stop sending messages after 5 minutes
                    scheduler.schedule(() -> {
                        scheduler.shutdown();
                    }, 5, TimeUnit.MINUTES);

                    System.out.println("announced : " + announcedFlag);
                    lastAnnouncementTime = LocalDateTime.now();
                } else {
                    // Check if 1 hour has passed since the last announcement
                    Duration duration = Duration.between(lastAnnouncementTime, LocalDateTime.now());
                    if (duration.toMinutes() >= 30) {
                        announcedFlag = 0;  // Reset the flag
                        System.out.println("announcedFlag reset to 0");
                    }
                }




            }



        });



        // Update status to "CLOSED" for closed auctions
        allAuctions.forEach(auction -> {

            LocalDate auctionDate = auction.getSlot().getDate();
            LocalTime thirtyMinutesBeforeEndTime = auction.getSlot().getEndTime().minusMinutes(30);
            LocalTime EndTime = auction.getSlot().getEndTime();

            LocalDateTime dateTimeForQueuedStatus = auctionDate.atTime(thirtyMinutesBeforeEndTime);
            LocalDateTime dateTimeForQueuedStatus2 = auctionDate.atTime(EndTime);

            if (currentDateTime.isAfter(dateTimeForQueuedStatus2)) {

                auction.setAuctionStatus(AuctionStatus.CLOSED);
                auctionRepository.save(auction);




            }



        });
    }
}
