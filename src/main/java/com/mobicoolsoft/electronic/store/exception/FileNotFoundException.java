package com.mobicoolsoft.electronic.store.exception;

import java.io.IOException;

public class FileNotFoundException extends IOException {
     String path;

     private String userId;

    public FileNotFoundException(String userId) {
        super(String.format("No image file found for Id : {}!",userId));
        this.userId = userId;
    }
}
