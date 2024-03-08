package com.herovired.Auction.Management.System.controllers;

import com.herovired.Auction.Management.System.dto.AuctionSlotResponse;
import com.herovired.Auction.Management.System.exception.CustomResponse;
import com.herovired.Auction.Management.System.models.RegistrationHistory;
import com.herovired.Auction.Management.System.models.UserAuctionRegistration;
import com.herovired.Auction.Management.System.repositories.RegistrationHistoryRepository;
import com.herovired.Auction.Management.System.repositories.UserAuctionRegistrationRepository;
import com.herovired.Auction.Management.System.services.UserAuctionRegistrationService;
import com.herovired.Auction.Management.System.services.impl.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auction-registrations")
public class UserAuctionRegistrationController {


    @Autowired
    private UserAuctionRegistrationRepository userAuctionRegistrationRepository;
    private final UserAuctionRegistrationService registrationService;

    @Autowired
    private RegistrationHistoryRepository registrationHistoryRepository;

    @Autowired
    private AccountServiceImpl accountService;

    @Autowired
    public UserAuctionRegistrationController(UserAuctionRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    // Endpoint to register a user for an auction
    @PostMapping("/register")
    public ResponseEntity<?> registerUserForAuction(@RequestParam String userId, @RequestParam String auctionId) {
        registrationService.registerUserForAuction(userId, auctionId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User Registered Successfully");
        return new ResponseEntity<>(response , HttpStatus.ACCEPTED);
    }

    // Endpoint to get all registrations
    @GetMapping("/registration/{userId}")
    public ResponseEntity<?> getRegistrationByUserId(@PathVariable String userId , @RequestParam int flag,@RequestParam(defaultValue = "0") int page){
        if(flag == 0) {
            var allRegistrations = userAuctionRegistrationRepository.findByUserUserId(userId);
            List<String> allRegistrationsName = new ArrayList<>();
            for (UserAuctionRegistration userAuctionRegistration : allRegistrations) {
                allRegistrationsName.add(userAuctionRegistration.getAuction().getAuctionId());
            }
            return new ResponseEntity<>(allRegistrationsName, HttpStatus.ACCEPTED);
        }
        if(flag == 1){
            var allRegistrations = registrationHistoryRepository.findByUserUserId(userId);
            List<AuctionSlotResponse> allAuctions = new ArrayList<>();
            for (RegistrationHistory registrationHistory : allRegistrations) {
                allAuctions.add(accountService.getAuctionResponseById(registrationHistory.getAuction().getAuctionId()));
            }
            Page<AuctionSlotResponse> pageAuctions = paginateList(allAuctions, page, 10);
            return new ResponseEntity<>(pageAuctions, HttpStatus.ACCEPTED);        }
                return new ResponseEntity<>("",HttpStatus.ACCEPTED);
}
    private <T> Page<T> paginateList(List<T> list, int page, int pageSize) {
        int start = page * pageSize;
        int end = Math.min((page + 1) * pageSize, list.size());
        return new PageImpl<>(list.subList(start, end), PageRequest.of(page, pageSize), list.size());
    }

    @GetMapping("/get-registered-users/{auctionId}")
    public ResponseEntity<?> getRegisteredUsersByAuctionId(@PathVariable String auctionId){
        List<UserAuctionRegistration> registeredUsers = userAuctionRegistrationRepository.findByAuctionAuctionId(auctionId);
        List<String> registeredUsernames = new ArrayList<>();

        for(UserAuctionRegistration userAuctionRegistration:registeredUsers){
            registeredUsernames.add(userAuctionRegistration.getUser().getUserId());
        }
        return ResponseEntity.ok(registeredUsernames);
    }

    @DeleteMapping("/unregister")
    public ResponseEntity<?> unregisterUserFromAuction(@RequestParam String userId, @RequestParam String auctionId , @RequestParam  int flag) {

        if(flag == 1) {
            userAuctionRegistrationRepository.deleteByUsernameAndAuctionId(userId, auctionId);
            registrationHistoryRepository.deleteByUsernameAndAuctionId(userId, auctionId);
            return new ResponseEntity<>(
                    new CustomResponse("UnRegistered User successfully", true), HttpStatus.OK);
        }
        else{
            userAuctionRegistrationRepository.deleteByUsernameAndAuctionId(userId, auctionId);
            return new ResponseEntity<>(
                    new CustomResponse("UnRegistered User successfully", true), HttpStatus.OK);
        }
    }
}

