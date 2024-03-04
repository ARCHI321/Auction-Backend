package com.herovired.Auction.Management.System.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomPageResponse {
    private long totalElements;
    private int totalPages;
    private int pageSize;
    private int currentPage;
    private List<AuctionSlotResponse> content;



}