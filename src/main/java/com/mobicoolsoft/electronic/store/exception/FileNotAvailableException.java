package com.mobicoolsoft.electronic.store.exception;

import java.io.IOException;

public class FileNotAvailableException extends IOException {
     private String userId;

    public FileNotAvailableException(String userId) {
        super(String.format("No image file found for Id : %s",userId));
        this.userId = userId;
    }
}
