package com.herovired.Auction.Management.System.services;

import com.herovired.Auction.Management.System.models.RegistrationHistory;
import com.herovired.Auction.Management.System.repositories.AuctionRepository;
import com.herovired.Auction.Management.System.repositories.RegistrationHistoryRepository;
import com.herovired.Auction.Management.System.repositories.UserAuctionRegistrationRepository;
import com.herovired.Auction.Management.System.models.Auction;
import com.herovired.Auction.Management.System.models.User;
import com.herovired.Auction.Management.System.models.UserAuctionRegistration;
import com.herovired.Auction.Management.System.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAuctionRegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private RegistrationHistoryRepository registrationHistoryRepository;

    private final UserAuctionRegistrationRepository registrationRepository;

    @Autowired
    public UserAuctionRegistrationService(UserAuctionRegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    // Example: Register a user for an auction
    public void registerUserForAuction(String userId, String auctionId) {

        var user = userRepository.findByUserId(userId);
        var auction = auctionRepository.findByAuctionId(auctionId);
        UserAuctionRegistration registration = new UserAuctionRegistration();
        registration.setUser(user.get());
        registration.setAuction(auction);
        var userAuctionRegistration = registrationRepository.save(registration);

        RegistrationHistory registrationHistory = new RegistrationHistory();
        registrationHistory.setAuction(userAuctionRegistration.getAuction());
        registrationHistory.setUser(userAuctionRegistration.getUser());
        registrationHistoryRepository.save(registrationHistory);
    }

    public void getRegistrationByUserId(String auctionId){

    }


    @Transactional
    public List<User> findUsersByAuctionId(String auctionId) {
        //System.out.println(auctionId);
        List<UserAuctionRegistration> registrations = registrationRepository.findByAuction_AuctionId(auctionId);
        //System.out.println(registrations);
        //System.out.println(registrations.stream()
               // .map(UserAuctionRegistration::getUser)
                //.collect(Collectors.toList()));
        return registrations.stream()
                .map(UserAuctionRegistration::getUser)
                .collect(Collectors.toList());
    }

    // Other service methods as needed
}

