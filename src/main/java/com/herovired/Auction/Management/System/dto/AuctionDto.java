package com.herovired.Auction.Management.System.dto;

import com.herovired.Auction.Management.System.models.AuctionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class AuctionDto {
    private String title;
    private String description;
    private long startingPrice;
    private String category;
    private AuctionType auctionType;
    private LocalDate date;
    private int slotNumber;
    private String sellerId;
    private Long registerFee;
    private MultipartFile file;
}