package com.herovired.Auction.Management.System.services.impl;

import com.herovired.Auction.Management.System.dto.AuctionDto;
import com.herovired.Auction.Management.System.dto.AuctionResponse;
import com.herovired.Auction.Management.System.dto.AuctionSlotResponse;
import com.herovired.Auction.Management.System.dto.FrontPageDto;
import com.herovired.Auction.Management.System.exception.*;
import com.herovired.Auction.Management.System.mapper.AuctionMapper;
import com.herovired.Auction.Management.System.models.*;
import com.herovired.Auction.Management.System.repositories.AuctionRepository;
import com.herovired.Auction.Management.System.repositories.SlotRepository;
import com.herovired.Auction.Management.System.repositories.TransactionRepository;
import com.herovired.Auction.Management.System.services.IAuctionService;
import com.herovired.Auction.Management.System.util.*;

import java.time.LocalTime;
import java.util.Comparator;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;


@Service
@AllArgsConstructor
public class AccountServiceImpl implements IAuctionService {

    private AuctionRepository auctionRepository;
    private final SlotRepository slotRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public AuctionResponse createAuction(AuctionDto auctionDto) throws IOException {
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime auctionStartDateTime = auctionDto.getDate().atTime(TimeGetter.getTime(auctionDto.getSlotNumber()));

        if (!auctionStartDateTime.isAfter(currentDateTime)) {
            throw new AuctionInFutureException("Auction should start in the future");
        }

        Slot existingSlot = slotRepository.findByDateAndSlotNumber(auctionDto.getDate(), auctionDto.getSlotNumber());
        if (existingSlot != null && existingSlot.getSlotStatus() != SlotStatus.AVAILABLE) {
            throw new SlotBookingException("Slot is not available");
        }


        String generatedAuctionId = generateUniqueAuctionId();
        Auction auction = Auction.builder()
                .auctionId(generatedAuctionId)
                .title(auctionDto.getTitle())
                .description(auctionDto.getDescription())
                .startingPrice(auctionDto.getStartingPrice())
                .currentPrice(auctionDto.getStartingPrice())
                .category(auctionDto.getCategory())
                .registerFee(auctionDto.getRegisterFee())
                .auctionType(auctionDto.getAuctionType())
                .sellerId(auctionDto.getSellerId())
                .auctionStatus(AuctionStatus.UPCOMING)
                .slot(Slot.builder()
                        .slotNumber(auctionDto.getSlotNumber())
                        .date(auctionDto.getDate())
                        .startTime(TimeGetter.getTime(auctionDto.getSlotNumber()))
                        .endTime(TimeGetter.getTime(auctionDto.getSlotNumber()).plusHours(1))
                        .slotStatus(SlotStatus.NOT_AVAILABLE)
                        .build())
                .build();

        if (auctionDto.getFile() != null && !auctionDto.getFile().isEmpty()) {
            auction.setImages(Images.builder()
                    .fileName(auctionDto.getFile().getOriginalFilename())
                    .type(auctionDto.getFile().getContentType())
                    .imageData(ImageUtils.compressImage(auctionDto.getFile().getBytes()))
                    .build());
        } else {
            throw new IOException("Unable to upload image");
        }

        auctionRepository.save(auction);
        return AuctionMapper.mapToAuctionResponse(auction);
    }

    private String generateUniqueAuctionId() {
        String generatedAuctionId;
        AlphaNumericIdGenerator idGenerator = new AlphaNumericIdGenerator();
        do {
            generatedAuctionId = (String) idGenerator.generate(null, null);
        } while (auctionRepository.existsById(generatedAuctionId));
        return generatedAuctionId;
    }


    @Override
    public AuctionDto updateAuction(AuctionDto auctionDto, String auctionId) throws AuctionClosedForUpdateException {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionNotFoundException("Auction", "ID", auctionId));
        if (auction.getAuctionStatus() == AuctionStatus.CLOSED) {
            throw new AuctionClosedForUpdateException("Auction is closed and cannot be updated");
        }

        auction.setStartingPrice(auctionDto.getStartingPrice());
        auction.setTitle(auctionDto.getTitle());
        auction.setDescription((auctionDto.getDescription()));
        auction.setCategory(auctionDto.getCategory());

        auction = auctionRepository.save(auction);

        return AuctionMapper.mapToAuctionDto(auction);
    }

    @Override
    public void deleteAuction(String auctionId) throws AuctionClosedForUpdateException {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionNotFoundException("Auction", "ID", auctionId));
        if (auction.getAuctionType() == AuctionType.Paid || auction.getAuctionStatus() == AuctionStatus.ACTIVE) {
            throw new AuctionClosedForUpdateException("Auction cannot be deleted");
        }
        auctionRepository.delete(auction);
    }

    @Override
    public Page<AuctionSlotResponse> getAllAuction(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return auctionRepository.findAll(pageable).map(AuctionMapper::mapToAuctionSlotResponse);
    }

    @Override
    public AuctionResponse getAuctionById(String auctionId) {
        return auctionRepository.findById(auctionId)
                .map(AuctionMapper::mapToAuctionResponse)
                .orElseThrow(() -> new AuctionNotFoundException("Auction", "ID", auctionId));
    }

    @Override
    public AuctionSlotResponse getAuctionResponseById(String auctionId) {
        return auctionRepository.findById(auctionId)
                .map(AuctionMapper::mapToAuctionSlotResponse)
                .orElseThrow(() -> new AuctionNotFoundException("Auction", "ID", auctionId));
    }

    @Override
    public Page<AuctionSlotResponse> getAllAuctionByDate(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Auction> auctionPage = auctionRepository.findAllAuctionsByDate(LocalDate.now(), pageable);

        if (auctionPage.isEmpty()) {
            throw new AuctionNotFoundException("Auctions", "date", LocalDate.now());
        }

        return auctionPage.map(AuctionMapper::mapToAuctionSlotResponse);
    }


    @Override
    public Page<AuctionResponse> getAuctionByCategory(String category, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return auctionRepository.findByCategory(category, pageable).map(AuctionMapper::mapToAuctionResponse);
    }

    @Override
    public Page<AuctionResponse> getAuctionBySellerId(String sellerId, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return auctionRepository.findBySellerId(sellerId, pageable).map(AuctionMapper::mapToAuctionResponse);
    }

    @Override
    public Page<AuctionSlotResponse> getAuctionResponseBySellerId(String sellerId, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return auctionRepository.findBySellerId(sellerId, pageable).map(AuctionMapper::mapToAuctionSlotResponse);
    }

    @Override
    public List<AuctionResponse> getAuctionByWinnerId(String winnerId) {
        return auctionRepository.findByWinnerId(winnerId)
                .stream()
                .map(AuctionMapper::mapToAuctionResponse)
                .collect(toList());
    }

    @Override
    public byte[] downloadImage(String auctionId) {
        Optional<Auction> auction = auctionRepository.findById(auctionId);
        return ImageUtils.decompressImage(auction.get().getImages().getImageData());
    }

//    public List<Auction> getTop5UpcomingAuctions() {
//        LocalDate currentDate = LocalDate.now();
//        return auctionRepository.findTop5UpcomingAuctions(AuctionStatus.UPCOMING,currentDate);
//    }

    public List<FrontPageDto> getTop5UpcomingAuctions() {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        List<Auction> allAuctions = auctionRepository.findAll();


        // Filter auctions based on status (e.g., Open) and future start date/time
        List<FrontPageDto> upcomingAuctions = allAuctions.stream()
                .filter(auction -> auction.getAuctionStatus() == AuctionStatus.UPCOMING)
                .sorted(Comparator.comparing(auction -> LocalDateTime.of(auction.getSlot().getDate(),
                        auction.getSlot().getStartTime())))
                .limit(5)
                .map(auction -> {
                    return FrontPageDto.builder()
                            .title(auction.getTitle())
                            .description(auction.getDescription())
                            .startingPrice(auction.getStartingPrice())
                            .startTime(auction.getSlot().getStartTime())
                            .build();
                })
                .toList();

        return upcomingAuctions;
    }

    public CustomResponse checkWinnerAndPaymentStatus(String auctionId) {
        // Find the auction by auctionId
        Optional<Auction> optionalAuction = auctionRepository.findById(auctionId);
        if (optionalAuction.isPresent()) {
            Auction auction = optionalAuction.get();

            // Check if there is a winner
            String winnerId = auction.getWinnerId();
            if (winnerId != null && !winnerId.isEmpty()) {

                // Find the winner's transaction for the specific auction
                Optional<Transaction> optionalTransaction = transactionRepository.findByUser_UserNameAndAuction_AuctionIdAndType(
                        winnerId, auctionId, TransactionType.AUCTIONPAYMENT);

                if (optionalTransaction.isPresent()) {
                    Transaction transaction = optionalTransaction.get();

                    // Check the payment status
                    if (transaction.getStatus() == TransactionStatus.SUCCESS) {
                        System.out.println("Winner: " + winnerId);
                        System.out.println("Payment Status: Paid");
                        return new CustomResponse(winnerId , true);
                    } else {
                        System.out.println("Winner: " + winnerId);
                        System.out.println("Payment Status: Not Paid");
                        return new CustomResponse(winnerId , false);
                    }
                } else {
                    System.out.println("Winner: " + winnerId);
                    System.out.println("Payment Status: Not Paid");
                    return new CustomResponse(winnerId , false);
                }
            } else {
                System.out.println("No winner for the auction yet.");
                return new CustomResponse("No winner found" , false);
            }
        } else {
            System.out.println("Auction with ID " + auctionId + " not found.");
            return new CustomResponse("Auction not found" , false);
        }


    }

}