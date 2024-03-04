package com.herovired.Auction.Management.System.scheduler;



import com.herovired.Auction.Management.System.services.AuctionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AuctionStatusUpdater {

    private final AuctionService auctionService;

    public AuctionStatusUpdater(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @Scheduled(cron = "0 0/1 * * * ?") // Run daily at midnight
//    @Scheduled(cron = "0 49 22 * * ?")
    public void updateAuctionStatus() {
        // Implement logic to update auction statuses
        auctionService.updateAuctionStatuses();
    }
}

