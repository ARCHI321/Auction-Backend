package com.herovired.Auction.Management.System.services.impl;

import com.herovired.Auction.Management.System.dto.AuctionDto;
import com.herovired.Auction.Management.System.dto.AuctionResponse;
import com.herovired.Auction.Management.System.dto.AuctionSlotResponse;
import com.herovired.Auction.Management.System.exception.AuctionClosedForUpdateException;
import com.herovired.Auction.Management.System.exception.AuctionInFutureException;
import com.herovired.Auction.Management.System.exception.AuctionNotFoundException;
import com.herovired.Auction.Management.System.exception.SlotBookingException;
import com.herovired.Auction.Management.System.mapper.AuctionMapper;
import com.herovired.Auction.Management.System.models.*;
import com.herovired.Auction.Management.System.repositories.AuctionRepository;
import com.herovired.Auction.Management.System.repositories.SlotRepository;
import com.herovired.Auction.Management.System.services.IAuctionService;
import com.herovired.Auction.Management.System.util.AlphaNumericIdGenerator;
import com.herovired.Auction.Management.System.util.ImageUtils;
import com.herovired.Auction.Management.System.util.TimeGetter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class AccountServiceImpl implements IAuctionService {

    private AuctionRepository auctionRepository;
    private final SlotRepository slotRepository;

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
                .collect(Collectors
                        .toList());
    }

    @Override
    public byte[] downloadImage(String auctionId) {
        Optional<Auction> auction = auctionRepository.findById(auctionId);
        return ImageUtils.decompressImage(auction.get().getImages().getImageData());
    }
}