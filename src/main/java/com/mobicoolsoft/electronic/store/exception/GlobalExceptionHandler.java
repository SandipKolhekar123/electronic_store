package com.mobicoolsoft.electronic.store.exception;

import com.mobicoolsoft.electronic.store.dto.ApiResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> resourceNotFound( ResourceNotFoundException ex){
        String message = ex.getMessage();
        ApiResponseMessage apiResponseMessage = new ApiResponseMessage(message, false, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(apiResponseMessage, HttpStatus.NOT_FOUND);
    }
}
