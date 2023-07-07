package com.mobicoolsoft.electronic.store.service.impl;

import com.mobicoolsoft.electronic.store.dto.UserDto;
import com.mobicoolsoft.electronic.store.entity.User;
import com.mobicoolsoft.electronic.store.exception.ResourceNotFoundException;
import com.mobicoolsoft.electronic.store.repository.UserRepository;
import com.mobicoolsoft.electronic.store.service.UserServiceI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
/**
 * @author Sandip Kolhekar
 * @apiNote User services implementing  class
 * @since 2021
 */
@Service
public class UserServiceImpl implements UserServiceI {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;
    /**
     * @implNote create new user
     */
    @Override
    public UserDto createUser(UserDto userDto) {
        //generate unique id in string format. A class that represents an immutable
        // universally unique identifier (UUID). A UUID represents a 128-bit value.
        logger.info("createUser service execution started");
        String userId = UUID.randomUUID().toString();
        logger.info("userId generated : {}", userId);
        userDto.setUserId(userId);
        User user = this.userDtoToEntity(userDto);
        User savedUser = this.userRepository.save(user);
        logger.info("saved user successfully");
        UserDto savedUserDto = this.entityToUserDto(savedUser);
        logger.info("createUser service execution ended...");
        return savedUserDto;
    }

    /**
     * @implNote update individual user
     */
    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        logger.info("updateUser service execution started");
        User user = this.userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", "UserID", userId));
        logger.info("User find for Id {}", userId);
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail().trim());
        user.setPassword(userDto.getPassword().trim());
        user.setAbout(userDto.getAbout());
        user.setGender(userDto.getGender());
        user.setImage(userDto.getImage());
        User savedUser = this.userRepository.save(user);
        logger.info("User saved successfully");
        UserDto updatedUserDto = this.entityToUserDto(savedUser);
        logger.info("updateUser service execution ended");
        return updatedUserDto;
    }

    /**
     * @implNote delete user by id
     */
    @Override
    public void deleteUser(String userId) {
        logger.info("deleteUser service execution started");
        User user = this.userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", "UserID", userId));
        this.userRepository.delete(user);
        logger.info("User deleted  with userId : {}", userId);
        logger.info("deleteUser service execution ended");
    }
    /**
     * @implNote get all users
     */
    @Override
    public List<UserDto> getAllUsers() {
        logger.info("getAllUsers service execution started");
        List<User> users = this.userRepository.findAll();
        logger.info("users get successfully");
        List<UserDto> userDtos = users.stream().map((user) -> this.entityToUserDto(user)).collect(Collectors.toList());
        logger.info("getAllUsers service execution ended");
        return userDtos;
    }

    /**
     * @implNote get user by id
     */
    @Override
    public UserDto getUserById(String userId) {
        logger.info("getUserById service execution started");
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "UserID", userId));
        logger.info("User found successfully for userId : {}",userId);
        UserDto userDto = this.entityToUserDto(user);
        logger.info("getUserById service execution ended");
        return userDto;
    }

    /**
     * @implNote get user by email
     */
    @Override
    public UserDto getUserByEmail(String email) {
        logger.info("getUserByEmail service execution started");
        User user = this.userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User", "Email", email));
        logger.info("User found successfully for email : {}",email);
        UserDto userDto = this.entityToUserDto(user);
        logger.info("getUserByEmail service execution started");
        return userDto;
    }

    /**
     * @implNote search user
     */
    @Override
    public UserDto getUserByEmailAndPassword(String email, String password) {
        logger.info("getUserByEmailAndPassword service execution started");
        User user = this.userRepository.findByEmailAndPassword(email, password).orElseThrow(() -> new ResourceNotFoundException("User", "Email", email));
        logger.info("User found successfully for email : {}", email);
        UserDto userDto = this.entityToUserDto(user);
        logger.info("getUserByEmailAndPassword service execution ended");
        return userDto;
    }

    /**
     * @implNote search user
     */
    @Override
    public List<UserDto> byNameContaining(String keyword) {
        logger.info("getUserByKeyword service execution started");
        List<User> users = this.userRepository.findByNameContaining(keyword);
        logger.info("User found for matched keyword : {}", keyword);
        List<UserDto> userDtos = users.stream().map((user) -> this.entityToUserDto(user)).collect(Collectors.toList());
        logger.info("getUserByKeyword service execution ended");
        return userDtos;
    }

    /**
     * @implNote builder design pattern to build User and UseDto objects
     */
    public User userDtoToEntity(UserDto userDto) {
        User user = User.builder()
                .userId(userDto.getUserId())
                .name(userDto.getName())
                .email(userDto.getEmail().trim())
                .password(userDto.getPassword().trim())
                .about(userDto.getAbout())
                .gender(userDto.getGender())
                .image(userDto.getImage())
                .build();
        return user;
    }
    public UserDto entityToUserDto(User user) {
        UserDto userDto = UserDto.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .about(user.getAbout())
                .gender(user.getGender())
                .image(user.getImage())
                .build();
        return userDto;
    }
}
