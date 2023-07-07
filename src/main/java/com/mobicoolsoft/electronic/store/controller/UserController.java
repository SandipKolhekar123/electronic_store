package com.mobicoolsoft.electronic.store.controller;

import com.mobicoolsoft.electronic.store.config.AppConstants;
import com.mobicoolsoft.electronic.store.dto.ApiResponseMessage;
import com.mobicoolsoft.electronic.store.dto.UserDto;
import com.mobicoolsoft.electronic.store.service.UserServiceI;
import com.mobicoolsoft.electronic.store.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * @apiNote create new user record in database
     * @implNote method input user object and return user record
     * @return user record
     */
    @PostMapping("/")
    public ResponseEntity<UserDto> createNewUser(@Valid @RequestBody UserDto userDto){
        logger.info("Api createNewUser request started");
        UserDto newUserDto = this.userServiceI.createUser(userDto);
        logger.info("Api createNewUser request ended with response : {}", HttpStatus.CREATED);
        return new ResponseEntity<>(newUserDto, HttpStatus.CREATED);
    }

    /**
     * @apiNote update existing user record of specified user id from database
     * @implNote method input user object, user id and return updated user record
     * @return  updated user record
     */
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable String userId){
        logger.info("Api updateUser request started for user with userId : {}", userId);
        UserDto updatedUserDto = this.userServiceI.updateUser(userDto, userId);
        logger.info("Api updateUser request ended with response : {}", HttpStatus.OK);
        return new ResponseEntity<>(updatedUserDto, HttpStatus.OK);
    }

    /**
     * @apiNote delete user of specified id from the database
     * @implNote method input user id and return response as string for success
     * @return response in {message, success, status}
     *
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId){
        logger.info("Api deleteUser request for single user with userId : {}", userId);
        this.userServiceI.deleteUser(userId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder()
                .message(AppConstants.DELETE_MSG)
                .success(true)
                .status(HttpStatus.OK)
                .build();
        logger.info("Api deleteUser request ended with response : {}", HttpStatus.OK);
        return new ResponseEntity<ApiResponseMessage>(responseMessage, HttpStatus.OK);
    }
    /**
     * @apiNote get all existing user records from the database
     * @implNote method return list of users
     * @return user list
     */
    @GetMapping("/")
    public ResponseEntity<List<UserDto>> getAllUsers(){
        logger.info("Api getAllUsers request started");
        List<UserDto> userDtos = this.userServiceI.getAllUsers();
        logger.info("Api getAllUsers request ended with response : {}", HttpStatus.OK);
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    /**
     * @apiNote get user record for specified id
     * @implNote method input user id and return single record
     * @return single user
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String userId){
        logger.info("Api getUserById request for User with userId  : {}", userId);
        UserDto userDto = this.userServiceI.getUserById(userId);
        logger.info("Api getUserById request ended with response : {}", HttpStatus.OK);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    /**
     * @apiNote get user record for matched email
     * @implNote method input user email and return matched user record
     * @return single user
     */
    @GetMapping("/email")
    public ResponseEntity<UserDto> getUserByEmail( @RequestParam("email") String email){
        logger.info("Api getUserByEmail request started for email : {}", HttpStatus.OK);
        UserDto userDto = this.userServiceI.getUserByEmail(email);
        logger.info("Api getUserByEmail request ended with response : {}", HttpStatus.OK);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    /**
     * @apiNote get user record for matched email and password
     * @implNote method input user email, password and return user record
     * @return single user
     */
    @GetMapping("/user")
    public ResponseEntity<UserDto> getUserByEmailAndPassword(
            @RequestParam("email") String email,
            @RequestParam("password") String password){
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
    public ResponseEntity<List<UserDto>> byNameContaining(@PathVariable String keyword){
        logger.info("Api getUserByNameContaining request for User with keyword : {}", keyword);
        List<UserDto> userDtos = this.userServiceI.byNameContaining(keyword);
        logger.info("Api getUserByNameContaining request ended with response : {}", HttpStatus.OK);
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

}
