package com.example.bidding.exceptions;

public class WrongAuctionOwnerException extends RuntimeException {

    public WrongAuctionOwnerException(String message) {
        super(message);
    }

}
