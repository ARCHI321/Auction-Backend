package com.herovired.Auction.Management.System.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class TransactionDetails {
    private String orderId;
    private String currency;
    private Integer amount;
    private String key;
}