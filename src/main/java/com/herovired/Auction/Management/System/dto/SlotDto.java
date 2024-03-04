package com.herovired.Auction.Management.System.dto;

import com.herovired.Auction.Management.System.models.SlotStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SlotDto {
    private int slotNumber;
    private SlotStatus slotStatus;
    private LocalTime endTime;
    private LocalTime startTime;

}
