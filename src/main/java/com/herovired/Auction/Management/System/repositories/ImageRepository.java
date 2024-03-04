package com.herovired.Auction.Management.System.repositories;

import com.herovired.Auction.Management.System.models.Images;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Images, Long> {

    Images findByImageId(Long imageId);
}
