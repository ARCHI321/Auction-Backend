package com.herovired.Auction.Management.System.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuctionNotFoundException extends RuntimeException {
    String resourceName;
    String fieldName;
    Object fieldValue;

    public AuctionNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : %s", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
