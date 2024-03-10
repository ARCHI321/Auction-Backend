package com.herovired.Auction.Management.System.controllers;

import com.herovired.Auction.Management.System.dto.*;
import com.herovired.Auction.Management.System.exception.AuctionClosedForUpdateException;
import com.herovired.Auction.Management.System.exception.CustomResponse;
import com.herovired.Auction.Management.System.exception.InvalidSlotNumberException;
import com.herovired.Auction.Management.System.models.*;
import com.herovired.Auction.Management.System.repositories.AuctionRepository;
import com.herovired.Auction.Management.System.repositories.ImageRepository;
import com.herovired.Auction.Management.System.repositories.SlotRepository;
import com.herovired.Auction.Management.System.repositories.UserAuctionRegistrationRepository;
import com.herovired.Auction.Management.System.services.IAuctionService;
import com.herovired.Auction.Management.System.util.Categories;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/auction")
@AllArgsConstructor
public class AuctionController {



    private IAuctionService auctionService;

    @Autowired
    private AuctionRepository auctionRepository;



    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserAuctionRegistrationRepository userAuctionRegistrationRepository;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AuctionResponse> createAuction(
            @ModelAttribute @Valid AuctionDto auctionDto) throws Exception, InvalidSlotNumberException {
        AuctionResponse createdAuction = auctionService.createAuction(auctionDto);
        return new ResponseEntity<>(createdAuction, HttpStatus.CREATED);
    }

    @GetMapping("/image/{auctionId}")
    public ResponseEntity<?> downloadImage(@PathVariable String auctionId) {
        byte[] imageData = auctionService.downloadImage(auctionId);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(imageData);
    }

    @GetMapping("/slot/{slotNumber}")
    public ResponseEntity<SlotDto> getSlotByDateAndSlotNumber(
            @RequestParam String date,
            @PathVariable int slotNumber
    ) {


        LocalDate parsedDate = LocalDate.parse(date);


        Slot slot = slotRepository.findSlotStatusByDateAndSlotNumber(parsedDate, slotNumber);

        SlotDto slotDto = new SlotDto();


        if (slot != null) {
            slotDto.setSlotStatus(slot.getSlotStatus());
            slotDto.setStartTime(slot.getStartTime());
            slotDto.setEndTime(slot.getEndTime());
            slotDto.setSlotNumber(slotNumber);
            return ResponseEntity.ok(slotDto);
        } else {
            slotDto.setSlotStatus(SlotStatus.AVAILABLE);
            slotDto.setStartTime(null);
            slotDto.setEndTime(null);
            slotDto.setSlotNumber(slotNumber);
            return ResponseEntity.ok(slotDto);
        }
    }

    @PutMapping("/update/{auctionId}")
    public ResponseEntity<AuctionDto> updateAuction(
            @PathVariable("auctionId") String auctionId,
            @ModelAttribute @Valid AuctionDto auctionDto) throws AuctionClosedForUpdateException {

        var updatedAuction = auctionService.updateAuction(auctionDto, auctionId);
        System.out.println("updatedAuction "+updatedAuction);
        return new ResponseEntity<>(updatedAuction, HttpStatus.OK);
    }



    @DeleteMapping("/delete/{auctionId}")
    public ResponseEntity<CustomResponse> deleteAuction(
            @PathVariable String auctionId, @RequestParam String userId) throws AuctionClosedForUpdateException {
       // System.out.println(auctionId + " " + userId);


        if(checkAuctionByUser(auctionId).equals(userId)){
            userAuctionRegistrationRepository.deleteByUsernameAndAuctionId(userId,auctionId);
            auctionService.deleteAuction(auctionId);

            return new ResponseEntity<>(
                    new CustomResponse("Auction deleted successfully", true), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(
                    new CustomResponse("Auction is not posted by you", true), HttpStatus.OK);
        }

    }

    public String checkAuctionByUser(String auctionId){
        AuctionResponse auction = auctionService.getAuctionById(auctionId);
        String userId = auction.getSellerId();
        return userId;
    }

    @GetMapping("/all-auctions")
    public ResponseEntity<Page<AuctionSlotResponse>> getAllAuctions(@RequestParam int page) {
        Page<AuctionSlotResponse> allAuctions = auctionService.getAllAuction(page);
        return new ResponseEntity<>(allAuctions, HttpStatus.OK);
    }
    @GetMapping("/all")
    public ResponseEntity<Page<AuctionSlotResponse>> getAllAuctionsByCurrentDate(@RequestParam int page) {

        // Call the service method to get all auctions for the current date
        Page<AuctionSlotResponse> allAuctions = auctionService.getAllAuctionByDate(page);

        return new ResponseEntity<>(allAuctions, HttpStatus.OK);
    }



    @GetMapping("/{auctionId}")
    public ResponseEntity<AuctionResponse> getAuctionById(@PathVariable String auctionId) {
        AuctionResponse auction = auctionService.getAuctionById(auctionId);
        return new ResponseEntity<>(auction, HttpStatus.OK);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Page<AuctionResponse>> getAuctionByCategory(
            @PathVariable String category,
            @RequestParam int page) {
        Page<AuctionResponse> auctionsByCategory = auctionService.getAuctionByCategory(category, page);
        return new ResponseEntity<>(auctionsByCategory, HttpStatus.OK);
    }

    @GetMapping("/types")
    public AuctionType[] getAllAuctionTypes() {
        return AuctionType.values();
    }

    @GetMapping(value = "/seller/{sellerId}" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomPageResponse> getAuctionsBySeller(
            @PathVariable String sellerId, @RequestParam int page) {

        Page<AuctionSlotResponse> auctionsBySeller = auctionService.getAuctionResponseBySellerId(sellerId, page);
        System.out.println(auctionsBySeller);
        System.out.println(auctionsBySeller.getContent());

        // Create a custom response object
        CustomPageResponse customResponse = new CustomPageResponse();
        customResponse.setPageSize(auctionsBySeller.getSize());
        customResponse.setCurrentPage(auctionsBySeller.getNumber()+1);
        customResponse.setContent(auctionsBySeller.getContent());
        customResponse.setTotalPages(auctionsBySeller.getTotalPages());
        customResponse.setTotalElements(auctionsBySeller.getTotalElements());

        // Return the custom response
        return ResponseEntity.ok(customResponse);
    }

    @GetMapping("/winner/{winnerId}")
    public ResponseEntity<List<AuctionResponse>> getAuctionByWinnerId(@PathVariable String winnerId) {
        List<AuctionResponse> auctionsByWinner = auctionService.getAuctionByWinnerId(winnerId);
        return new ResponseEntity<>(auctionsByWinner, HttpStatus.OK);
    }

//    @GetMapping("/winner-has-paid")
//    public ResponseEntity<?> checkWinnerAndPaymentStatus(){
//        auctionService.checkWinnerAndPaymentStatus();
//    }
}
