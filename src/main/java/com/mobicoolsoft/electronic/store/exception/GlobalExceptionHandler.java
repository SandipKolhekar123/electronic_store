package com.mobicoolsoft.electronic.store.exception;

import com.mobicoolsoft.electronic.store.config.AppConstants;
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
        logger.info("ResourceNotFoundException called");
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
        logger.info("IllegalArgumentException called");
        ApiResponseMessage responseMessage = ApiResponseMessage.builder()
                .message(AppConstants.PAGE_ERROR_MSG)
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
        logger.info("MethodArgumentNotValidException called");
        Map<String, String> resp = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            resp.put(fieldName, message);
            logger.info("ResourceNotFoundException handled with response {}", HttpStatus.BAD_REQUEST);
        });
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }
    /**
     * @author Sandip Kolhekar
     * @param ex instance of StringIndexOutOfBoundsException
     * @return apiResponseMessage instance of ApiResponseMessage
     * @implNote StringIndexOutOfBoundsException handle globally through @ExceptionHandler
     */
    @ExceptionHandler(StringIndexOutOfBoundsException.class)
    public ResponseEntity<ApiResponseMessage> handlerStringIndexOutOfBoundsException(StringIndexOutOfBoundsException ex) {
        logger.info("StringIndexOutOfBoundsException called for empty file with index -1");
        ApiResponseMessage responseMessage = ApiResponseMessage.builder()
                .message(ex.getMessage())
                .success(false)
                .status(HttpStatus.BAD_REQUEST)
                .build();
        logger.info("StringIndexOutOfBoundsException handled with response {}", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }

    /**
     * @author Sandip Kolhekar
     * @param ex instance of BadApiRequestException
     * @return apiResponseMessage instance of ApiResponseMessage
     * @implNote BadApiRequestException handle globally through @ExceptionHandler
     */
    @ExceptionHandler(BadApiRequestException.class)
    public ResponseEntity<ApiResponseMessage> handlerBadApiRequestException(BadApiRequestException ex) {
        logger.info("BadApiRequestException handler called");
        ApiResponseMessage responseMessage = ApiResponseMessage.builder()
                .message(ex.getMessage())
                .success(false)
                .status(HttpStatus.BAD_REQUEST)
                .build();
        logger.info("BadApiRequestException handled with response {}", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }

    /**
     * @author Sandip Kolhekar
     * @param ex instance of BadApiRequestException
     * @return apiResponseMessage instance of ApiResponseMessage
     * @implNote BadApiRequestException handle globally through @ExceptionHandler
     */
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> handlerFileNotFoundException(FileNotFoundException ex) {
        logger.info("FileNotFoundException handler called");
        ApiResponseMessage responseMessage = ApiResponseMessage.builder()
                .message(ex.getMessage())
                .success(false)
                .status(HttpStatus.NOT_FOUND)
                .build();
        logger.info("BadApiRequestException handled with response {}", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }

}
