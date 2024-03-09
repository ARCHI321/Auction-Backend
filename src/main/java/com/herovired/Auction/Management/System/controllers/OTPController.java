package com.herovired.Auction.Management.System.controllers;

import com.herovired.Auction.Management.System.dto.OtpRequest;
import com.herovired.Auction.Management.System.dto.OtpResponseDto;
import com.herovired.Auction.Management.System.dto.OtpValidationRequest;
import com.herovired.Auction.Management.System.exception.CustomResponse;
import com.herovired.Auction.Management.System.services.SMSService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/otp")
@Slf4j
public class OTPController {



    @Autowired
    private SMSService smsService;

    @GetMapping("/process")
    public String processSMS() {
        return "SMS sent";
    }

    @PostMapping("/send-otp")
    public OtpResponseDto sendOtp(@RequestBody OtpRequest otpRequest) {
        log.info("inside sendOtp :: "+otpRequest.getUsername());
        return smsService.sendSMS(otpRequest);
    }
    @PostMapping("/validate-otp")
    public ResponseEntity<?> validateOtp(@RequestBody OtpValidationRequest otpValidationRequest) {
        log.info("inside validateOtp :: "+otpValidationRequest.getUsername()+" "+otpValidationRequest.getOtpNumber());
        String msg = smsService.validateOtp(otpValidationRequest);
        return new ResponseEntity<>(
                new CustomResponse(msg, true), HttpStatus.OK);
    }
}

