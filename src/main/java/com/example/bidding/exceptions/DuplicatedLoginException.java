package com.example.bidding.exceptions;

public class DuplicatedLoginException extends RuntimeException {

    public DuplicatedLoginException(String message) {
        super(message);
    }

}
