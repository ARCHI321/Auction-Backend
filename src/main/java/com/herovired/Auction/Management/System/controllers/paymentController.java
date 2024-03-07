package com.herovired.Auction.Management.System.controllers;



import com.herovired.Auction.Management.System.exception.CustomResponse;
import com.herovired.Auction.Management.System.models.Transaction;
import com.herovired.Auction.Management.System.models.TransactionDetails;
import com.herovired.Auction.Management.System.repositories.AuctionRepository;
import com.herovired.Auction.Management.System.repositories.TransactionRepository;
import com.herovired.Auction.Management.System.repositories.UserAuctionRegistrationRepository;
import com.herovired.Auction.Management.System.repositories.UserRepository;
import com.herovired.Auction.Management.System.services.PaymentService;
import com.herovired.Auction.Management.System.util.TransactionStatus;
import com.herovired.Auction.Management.System.util.TransactionType;
import com.razorpay.RazorpayException;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@CrossOrigin("http://localhost:4200")
@RequestMapping("/payment")
@AllArgsConstructor
public class paymentController {

    private PaymentService paymentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserAuctionRegistrationRepository userAuctionRegistrationRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @GetMapping("/createTransaction/{amount}")
    public TransactionDetails createTransaction(@PathVariable(name = "amount") Long amount , @RequestParam boolean isRegistry , @RequestParam String userId , @RequestParam String auctionId) throws RazorpayException {


        var transactionDetails = paymentService.createTransaction(amount);
        var user = userRepository.findByUserId(userId);
        var auction = auctionRepository.findByAuctionId(auctionId);
        Transaction transaction = new Transaction();
        transaction.setUser(user.get());
        transaction.setAuction(auction);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setAmount(transactionDetails.getAmount());
        if(isRegistry == true){
            transaction.setType(TransactionType.REGISTRATION);
        }
        else{
            transaction.setType(TransactionType.AUCTIONPAYMENT);
        }
        transaction.setTransactionDateTime(LocalDateTime.now());
        transaction.setTransactionId(transactionDetails.getOrderId());

        transactionRepository.save(transaction);

        return transactionDetails;

    }
    @PostMapping("/set-status")
    public ResponseEntity<?> setStatusOfPayment(@RequestParam String userId,@RequestParam String auctionId ,@RequestParam String transactionId,@RequestParam boolean isFailure){
        System.out.println(transactionId);

        if(isFailure){
            transactionRepository.updateStatusByTransactionId(transactionId,TransactionStatus.FAILURE);
            userAuctionRegistrationRepository.deleteByUsernameAndAuctionId(userId , auctionId);
        }
        else {
            transactionRepository.updateStatusByTransactionId(transactionId,TransactionStatus.SUCCESS);
        }


        return  new ResponseEntity<>(
                new CustomResponse("Status set" , true), HttpStatus.ACCEPTED);
    }

}
