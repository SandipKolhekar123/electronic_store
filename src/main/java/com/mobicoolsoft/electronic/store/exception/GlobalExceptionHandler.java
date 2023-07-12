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
     * @param ex instance of ResourceNotFoundException
     * @return apiResponseMessage instance of ApiResponseMessage
     * @implNote ResourceNotFoundException handle globally through @ExceptionHandler
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> handlerResourceNotFoundException(ResourceNotFoundException ex) {
        logger.info("ResourceNotFoundException encounter");
        ApiResponseMessage responseMessage = ApiResponseMessage.builder()
                .message(ex.getMessage())
                .success(false)
                .status(HttpStatus.NOT_FOUND)
                .build();
        logger.info("ResourceNotFoundException handled with response {}", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(responseMessage, HttpStatus.NOT_FOUND);
    }
    /**
     * @author Sandip Kolhekar
     * @param ex instance of IllegalArgumentException
     * @return apiResponseMessage instance of ApiResponseMessage
     * @implNote IllegalArgumentException handle globally through @ExceptionHandler
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseMessage> handlerIllegalArgumentException(IllegalArgumentException ex) {
        logger.info("IllegalArgumentException encounter");
        ApiResponseMessage responseMessage = ApiResponseMessage.builder()
                .message("PageSize OR PageNumber must not be less than zero.")
                .success(false)
                .status(HttpStatus.NOT_FOUND)
                .build();
        logger.info("IllegalArgumentException handled with response {}", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(responseMessage, HttpStatus.NOT_FOUND);
    }
    /**
     * @author Sandip Kolhekar
     * @param ex instance of MethodArgumentNotValidException
     * @return resp an instance of Map<error_field, error_message>
     * @implNote MethodArgumentNotValidException handle globally through @ExceptionHandler
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handlerMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        logger.info("MethodArgumentNotValidException encounter");
        Map<String, String> resp = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            resp.put(fieldName, message);
            logger.info("ResourceNotFoundException handled with response {}", HttpStatus.BAD_REQUEST);
        });
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }
}
