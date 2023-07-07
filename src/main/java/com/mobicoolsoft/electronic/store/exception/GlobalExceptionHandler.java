package com.mobicoolsoft.electronic.store.exception;

import com.mobicoolsoft.electronic.store.dto.ApiResponseMessage;
import com.mobicoolsoft.electronic.store.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * @author Sandip Kolhekar
     * @implNote ResourceNotFoundException handle globally through @ExceptionHandler
     * @param ex instance of ResourceNotFoundException
     * @return apiResponseMessage instance of ApiResponseMessage
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> resourceNotFound( ResourceNotFoundException ex){
        logger.info("ResourceNotFoundException encounter");
        String message = ex.getMessage();
        ApiResponseMessage apiResponseMessage = new ApiResponseMessage(message, false, HttpStatus.NOT_FOUND);
        logger.info("ResourceNotFoundException handled with response {}", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(apiResponseMessage, HttpStatus.NOT_FOUND);
    }

    /**
     * @author Sandip Kolhekar
     * @implNote MethodArgumentNotValidException handle globally through @ExceptionHandler
     * @param ex instance of MethodArgumentNotValidException
     * @return resp an instance of Map<error_field, error_message>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> methodArgumentNotValidException(MethodArgumentNotValidException ex){
        logger.info("MethodArgumentNotValidException encounter");
        Map<String, String> resp = new HashMap<>();
            ex.getBindingResult().getAllErrors().forEach((error)->{
                String fieldName = ((FieldError) error).getField();
                String message = error.getDefaultMessage();
                resp.put(fieldName, message);
            logger.info("ResourceNotFoundException handled with response {}", HttpStatus.BAD_REQUEST);
        });
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }
}
