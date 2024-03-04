package com.herovired.Auction.Management.System.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "images")
public class Images {
    @Id
    @GeneratedValue
    private Long imageId;

    private String fileName;
    private String type;
    @Lob
    @Column(columnDefinition = "longblob")
    private byte[] imageData;

    public void processFile(MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            try {
                byte[] fileBytes = file.getBytes();

                this.imageData = fileBytes;

                this.fileName = file.getOriginalFilename();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
