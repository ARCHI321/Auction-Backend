package com.herovired.Auction.Management.System.controllers;

import com.herovired.Auction.Management.System.exception.CustomResponse;
import com.herovired.Auction.Management.System.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class NotificationController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send-notification")
    public ResponseEntity<?> sendNotification(@RequestParam String receiver , @RequestParam String subjectBody , @RequestBody String contentBody  ) {
        // Replace with your actual email details
        String to = receiver;
        String subject = subjectBody;
        String body = contentBody;

        emailService.sendEmail(to, subject, body);

        return new ResponseEntity<>(new CustomResponse("Email sent successfully!" , true) , HttpStatus.OK);
    }
}

