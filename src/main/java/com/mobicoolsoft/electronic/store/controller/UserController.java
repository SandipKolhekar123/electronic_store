package com.mobicoolsoft.electronic.store.controller;

import com.mobicoolsoft.electronic.store.config.AppConstants;
import com.mobicoolsoft.electronic.store.dto.ApiResponseMessage;
import com.mobicoolsoft.electronic.store.dto.UserDto;
import com.mobicoolsoft.electronic.store.service.UserServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Sandip Kolhekar
 * @apiNote build controller layer for user services
 * @since 2021
 */
@RestController
@RequestMapping(AppConstants.USER_URL)
public class UserController {
    @Autowired
    private UserServiceI userServiceI;

    /**
     * @apiNote create new user record in database
     * @implNote method input user object and return saved user record
     */
    @PostMapping("/")
    public ResponseEntity<UserDto> createNewUser(@RequestBody UserDto userDto){

        UserDto newUserDto = this.userServiceI.createUser(userDto);
        return new ResponseEntity<>(newUserDto, HttpStatus.CREATED);
    }

    /**
     * @apiNote update existing user record of specified user id from database
     * @implNote method input user object, user id and return updated user record
     */
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto, @PathVariable String userId){
        UserDto updatedUserDto = this.userServiceI.updateUser(userDto, userId);
        return new ResponseEntity<>(updatedUserDto, HttpStatus.OK);
    }

    /**
     * @apiNote delete user of specified id from the database
     * @implNote method input user id and return response as string for success
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId){
        this.userServiceI.deleteUser(userId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder()
                .message(AppConstants.DELETE_MSG)
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<ApiResponseMessage>(responseMessage, HttpStatus.OK);
    }


    /**
     * @apiNote get all existing user records from the database
     * @implNote method return list of users
     */
    @GetMapping("/")
    public ResponseEntity<List<UserDto>> getAllUsers(){
        List<UserDto> userDtos = this.userServiceI.getAllUsers();
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    /**
     * @apiNote get user record for specified id
     * @implNote method input user id and return single record
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String userId){
        UserDto userDto = this.userServiceI.getUserById(userId);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    /**
     * @apiNote get user record for matched email
     * @implNote method input user email and return matched user record
     */
    @GetMapping("/email")
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam("email") String email){
        UserDto userDto = this.userServiceI.getUserByEmail(email);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    /**
     * @apiNote get user record for matched email and password
     * @implNote method input user email, password and return user record
     */
    @GetMapping("/user")
    public ResponseEntity<UserDto> getUserByEmailAndPassword(
            @RequestParam("email") String email,
            @RequestParam("password") String password){
        UserDto userDto = this.userServiceI.getUserByEmailAndPassword(email, password);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    /**
     * @apiNote create new user record in database
     * @implNote method input user object and return saved user object
     */
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<UserDto>> byNameContaining(@PathVariable String keyword){
        List<UserDto> userDtos = this.userServiceI.byNameContaining(keyword);
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

}
