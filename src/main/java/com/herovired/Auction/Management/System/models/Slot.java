package com.herovired.Auction.Management.System.models;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "slot")
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "slot_id")
    private Long slotId;

    @Column(name = "slot_number")
    private int slotNumber;

    private LocalDate date;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "slot_status")
    private SlotStatus slotStatus;


}
