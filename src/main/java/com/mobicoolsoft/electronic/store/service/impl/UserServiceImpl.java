package com.mobicoolsoft.electronic.store.service.impl;

import com.mobicoolsoft.electronic.store.dto.UserDto;
import com.mobicoolsoft.electronic.store.entity.User;
import com.mobicoolsoft.electronic.store.exception.ResourceNotFoundException;
import com.mobicoolsoft.electronic.store.repository.UserRepository;
import com.mobicoolsoft.electronic.store.service.UserServiceI;
import lombok.Builder;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserServiceI {

    @Autowired
    private UserRepository userRepository;

    /**
     * @implNote create new user
     */
    @Override
    public UserDto createUser(UserDto userDto) {
        //generate unique id in string format. A class that represents an immutable
        // universally unique identifier (UUID). A UUID represents a 128-bit value.
        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);
        User user = this.userDtoToEntity(userDto);
        User savedUser = this.userRepository.save(user);
        UserDto savedUserDto = this.entityToUserDto(savedUser);
        return savedUserDto;
    }

    /**
     * @implNote update individual user
     */
    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        User user = this.userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", "UserID", userId));
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setAbout(userDto.getAbout());
        user.setGender(userDto.getGender());
        user.setImage(userDto.getImage());
        User savedUser = this.userRepository.save(user);
        System.out.println(savedUser);
        UserDto updatedUserDto = this.entityToUserDto(savedUser);
        return updatedUserDto;
    }

    /**
     * @implNote delete user by id
     */
    @Override
    public void deleteUser(String userId) {
        User user = this.userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", "UserID", userId));
        this.userRepository.delete(user);
    }

    /**
     * @implNote get all users
     */
    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = this.userRepository.findAll();
        List<UserDto> userDtos = users.stream().map((user) -> this.entityToUserDto(user)).collect(Collectors.toList());
        return userDtos;
    }

    /**
     * @implNote get user by id
     */
    @Override
    public UserDto getUserById(String userId) {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "UserID", userId));
        UserDto userDto = this.entityToUserDto(user);
        return userDto;
    }

    /**
     * @implNote get user by email
     */
    @Override
    public UserDto getUserByEmail(String email) {
        User user = this.userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User", "Email", email));
        UserDto userDto = this.entityToUserDto(user);
        return userDto;
    }

    /**
     * @implNote search user
     */
    @Override
    public UserDto getUserByEmailAndPassword(String email, String password) {
        User user = this.userRepository.findByEmailAndPassword(email, password).orElseThrow(() -> new ResourceNotFoundException("User", "Email", email));
        UserDto userDto = this.entityToUserDto(user);
        return userDto;
    }

    /**
     * @implNote search user
     */
    @Override
    public List<UserDto> byNameContaining(String keyword) {
        List<User> users = this.userRepository.findByNameContaining(keyword);
        List<UserDto> userDtos = users.stream().map((user) -> this.entityToUserDto(user)).collect(Collectors.toList());
        return userDtos;
    }

    /**
     * @implNote builder design pattern to build User and UseDto objects
     */
    public User userDtoToEntity(UserDto userDto) {
        User user = User.builder()
                .userId(userDto.getUserId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
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
