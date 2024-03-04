package com.herovired.Auction.Management.System.repositories;

import com.herovired.Auction.Management.System.dto.SlotDto;
import com.herovired.Auction.Management.System.models.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface SlotRepository extends JpaRepository<Slot, Long> {


    Slot findByDateAndSlotNumber(LocalDate date, int slotNumber);

    Slot findBySlotId(Long slotId);


//    @Query(value = "SELECT s.slot_status AS slotStatus, s.end_time AS endTime, s.start_time AS startTime FROM slot s WHERE s.date = :date AND s.slot_number = :slotNumber", nativeQuery = true)
//    Slot findSlotStatusByDateAndSlotNumber(@Param("date") LocalDate date, @Param("slotNumber") int slotNumber);
    Slot findSlotStatusByDateAndSlotNumber(LocalDate date, int slotNumber);

    Slot findSlotStatusByDate(LocalDate date);
}

