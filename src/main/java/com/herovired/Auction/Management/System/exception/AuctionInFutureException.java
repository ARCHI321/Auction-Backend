package com.herovired.Auction.Management.System.exception;

public class AuctionInFutureException extends RuntimeException {
    public AuctionInFutureException(String message) {
        super(message);
    }
}

