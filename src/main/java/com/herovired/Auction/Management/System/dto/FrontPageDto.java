package com.herovired.Auction.Management.System.dto;


import com.herovired.Auction.Management.System.models.Images;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FrontPageDto {
    private String title;
    private String description;
    private Long startingPrice;
    private LocalTime startTime;
}
