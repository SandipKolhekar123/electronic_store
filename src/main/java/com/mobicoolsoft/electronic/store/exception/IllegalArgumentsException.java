package com.mobicoolsoft.electronic.store.exception;

public class IllegalArgumentsException extends RuntimeException{


    private String message;

    public IllegalArgumentsException(String pageErrorMsg) {
        super(pageErrorMsg);
    }
}
