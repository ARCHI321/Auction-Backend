package com.herovired.Auction.Management.System.models;

import com.herovired.Auction.Management.System.util.TransactionStatus;
import com.herovired.Auction.Management.System.util.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "user_name")
    private User user;

    @ManyToOne
    private Auction auction;

    private String transactionId;

    private Integer amount;
    private LocalDateTime transactionDateTime;

    // Fields for transaction status and type
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Enumerated(EnumType.STRING)
    private TransactionType type;



    // getters and setters
}