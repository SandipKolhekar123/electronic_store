package com.mobicoolsoft.electronic.store.controller;

import com.mobicoolsoft.electronic.store.config.AppConstants;
import com.mobicoolsoft.electronic.store.dto.ApiResponseMessage;
import com.mobicoolsoft.electronic.store.dto.ImageResponse;
import com.mobicoolsoft.electronic.store.dto.UserDto;
import com.mobicoolsoft.electronic.store.dto.PageResponse;
import com.mobicoolsoft.electronic.store.exception.FileNotAvailableException;
import com.mobicoolsoft.electronic.store.service.FileServiceI;
import com.mobicoolsoft.electronic.store.service.UserServiceI;
import com.mobicoolsoft.electronic.store.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author Sandip Kolhekar
 * @apiNote build rest controller for user services
 * @since 2021
 */
@RestController
@RequestMapping(AppConstants.USER_URL)
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserServiceI userServiceI;

    @Autowired
    private FileServiceI fileServiceI;

    @Value("${user.profile.image.path}")
    private String imagePath;

    /**
     * @return user record
     * @apiNote create new user record in database
     * @implNote method input user object and return user record
     */
    @PostMapping("/")
    public ResponseEntity<UserDto> createNewUser(@Valid @RequestBody UserDto userDto) {
        logger.info("Api createNewUser request started");
        UserDto newUserDto = this.userServiceI.createUser(userDto);
        logger.info("Api createNewUser request ended with response : {}", HttpStatus.CREATED);
        return new ResponseEntity<>(newUserDto, HttpStatus.CREATED);
    }

    /**
     * @return updated user record
     * @apiNote update existing user record of specified user id from database
     * @implNote method input user object, user id and return updated user record
     */
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable String userId) {
        logger.info("Api updateUser request started for user with userId : {}", userId);
        UserDto updatedUserDto = this.userServiceI.updateUser(userDto, userId);
        logger.info("Api updateUser request ended with response : {}", HttpStatus.OK);
        return new ResponseEntity<>(updatedUserDto, HttpStatus.OK);
    }

    /**
     * @return response in {message, success, status}
     * @apiNote delete user of specified id from the database
     * @implNote method input user id and return response as string for success
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId) {
        logger.info("Api deleteUser request for single user with userId : {}", userId);
        this.userServiceI.deleteUser(userId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder()
                .message("User" + AppConstants.DELETE_MSG)
                .success(true)
                .status(HttpStatus.OK)
                .build();
        logger.info("Api deleteUser request ended with response : {}", HttpStatus.OK);
        return new ResponseEntity<ApiResponseMessage>(responseMessage, HttpStatus.OK);
    }

    /**
     * @return user list
     * @apiNote get all existing user records from the database
     * @implNote method return list of users
     */
    @GetMapping("/")
    public ResponseEntity<PageResponse<UserDto>> getAllUsers(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_USER_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {
        logger.info("Api getAllUsers request started");
        PageResponse<UserDto> pageResponse = this.userServiceI.getAllUsers(pageNumber, pageSize, sortBy, sortDir);
        logger.info("Api getAllUsers request ended with response : {}", HttpStatus.OK);
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    /**
     * @return single user
     * @apiNote get user record for specified id
     * @implNote method input user id and return single record
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String userId) {
        logger.info("Api getUserById request for User with userId  : {}", userId);
        UserDto userDto = this.userServiceI.getUserById(userId);
        logger.info("Api getUserById request ended with response : {}", HttpStatus.OK);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    /**
     * @return single user
     * @apiNote get user record for matched email
     * @implNote method input user email and return matched user record
     */
    @GetMapping("/email")
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam("email") String email) {
        logger.info("Api getUserByEmail request started for email : {}", HttpStatus.OK);
        UserDto userDto = this.userServiceI.getUserByEmail(email);
        logger.info("Api getUserByEmail request ended with response : {}", HttpStatus.OK);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    /**
     * @return single user
     * @apiNote get user record for matched email and password
     * @implNote method input user email, password and return user record
     */
    @GetMapping("/user")
    public ResponseEntity<UserDto> getUserByEmailAndPassword(
            @RequestParam("email") String email,
            @RequestParam("password") String password) {
        logger.info("Api getUserByEmailAndPassword request for User with email : {}", email);
        UserDto userDto = this.userServiceI.getUserByEmailAndPassword(email, password);
        logger.info("Api getUserByEmailAndPassword with response : {}", HttpStatus.OK);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    /**
     * @apiNote search user by keyword for username
     * @implNote method input user object and return saved user object
     */
    @GetMapping("/search/{keyword}")
    public ResponseEntity<PageResponse<UserDto>> byNameContaining(@PathVariable String keyword,
              @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
              @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
              @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_USER_BY, required = false) String sortBy,
              @RequestParam(name = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir)
    {
        logger.info("Api getUserByNameContaining request for User with keyword : {}", keyword);
        PageResponse<UserDto> pageResponse = this.userServiceI.byNameContaining(keyword, pageNumber, pageSize, sortBy, sortDir);
        logger.info("Api getUserByNameContaining request ended with response : {}", HttpStatus.OK);
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    /**
     * @param userImage
     * @param userId
     * @return ResponseEntity<ImageResponse>
     * @throws IOException
     * @implNote method to upload user image
     */
    @PostMapping("/images/upload/{userId}")
    public ResponseEntity<ImageResponse> uploadUserImage(
            @RequestParam("userImage") MultipartFile userImage,
            @PathVariable String userId) throws IOException
    {
        logger.info("Api uploadUserImage request with image : {}", userImage);
        UserDto userDto = this.userServiceI.getUserById(userId);
        logger.info("user found with userId : {}", userId);
        String uploadedUserImage = this.fileServiceI.uploadImage(imagePath, userImage);
        logger.info("user image successfully upload on server!");
        userDto.setImage(uploadedUserImage);
        this.userServiceI.updateUser(userDto, userId);
        logger.info("user image successfully saved in the database!");
        ImageResponse imageResponse = ImageResponse.builder()
                .imageName(uploadedUserImage).message("User"+AppConstants.IMAGE_MSG)
                .success(true).status(HttpStatus.CREATED).build();
        logger.info("Api uploadUserImage request ended with response : {}", HttpStatus.CREATED);
        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
    }

    @GetMapping(value = "/image/{userId}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws FileNotAvailableException {
        logger.info("Api serveUserImage request started with input response : {}", response);
        UserDto user = this.userServiceI.getUserById(userId);
        logger.info("get user image {}", user.getImage());
        try {
            InputStream serveImage = this.fileServiceI.serveImage(imagePath, user.getImage());
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            StreamUtils.copy(serveImage, response.getOutputStream());
        } catch (IOException ex) {
            logger.info("FileNotFoundException encounter");
            throw new FileNotAvailableException(userId);
        }
        logger.info("Api serveUserImage request ended with input response : {}", response);
    }
}
