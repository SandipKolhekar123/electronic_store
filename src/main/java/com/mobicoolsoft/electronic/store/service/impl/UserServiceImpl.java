package com.mobicoolsoft.electronic.store.service.impl;

import com.mobicoolsoft.electronic.store.config.AppConstants;
import com.mobicoolsoft.electronic.store.dto.UserDto;
import com.mobicoolsoft.electronic.store.entity.Role;
import com.mobicoolsoft.electronic.store.entity.User;
import com.mobicoolsoft.electronic.store.exception.IllegalArgumentsException;
import com.mobicoolsoft.electronic.store.exception.ResourceNotFoundException;
import com.mobicoolsoft.electronic.store.dto.PageResponse;
import com.mobicoolsoft.electronic.store.helper.PageHelper;
import com.mobicoolsoft.electronic.store.repository.RoleRepository;
import com.mobicoolsoft.electronic.store.repository.UserRepository;
import com.mobicoolsoft.electronic.store.service.UserServiceI;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.util.Date;
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

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    /**
     * @implNote create new user
     */
    @Override
    public UserDto createUser(UserDto userDto) {
//        generate unique id in string format. A class that represents an immutable
//         universally unique identifier (UUID). A UUID represents a 128-bit value.
        logger.info("createUser service execution started");
        String userId = UUID.randomUUID().toString();
        logger.info("userId generated : {}", userId);
        userDto.setUserId(userId);
//        userDto.setPassword(passwordEncoder.encode(userDto.getPassword().trim()));
        User user = this.modelMapper.map(userDto, User.class);
        Role role = this.roleRepository.findById(AppConstants.ROLE_USER).get();
        user.getRoles().add(role);
        User savedUser = this.userRepository.save(user);
        logger.info("user saved successfully");
        UserDto savedUserDto = this.modelMapper.map(savedUser, UserDto.class);
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
        UserDto updatedUserDto = this.modelMapper.map(savedUser, UserDto.class);
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
    public PageResponse<UserDto> getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        logger.info("getAllUsers service execution started");
        try{
            Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            //@implNote pageNumber default starts from 0
            Pageable pageable = PageRequest.of(pageNumber-1, pageSize, sort);
            logger.info("get Pageable object with pageNumber {}, pageSize {}", pageNumber, pageSize);
            Page<User> userPage = this.userRepository.findAll(pageable);
            logger.info("get Page object for User");
            PageResponse<UserDto> pageResponse = PageHelper.getPageResponse(userPage, UserDto.class);
            logger.info("get PageResponse<UserDto> process successfully");
            logger.info("getAllUsers service execution ended");
            return pageResponse;
        }catch (RuntimeException ex){
            logger.info("IllegalArgumentException encounter");
            throw new IllegalArgumentsException(AppConstants.PAGE_ERROR_MSG);
        }
    }

    /**
     * @implNote get user by id
     */
    @Override
    public UserDto getUserById(String userId) {
        logger.info("getUserById service execution started");
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "UserID", userId));
        logger.info("User found successfully for userId : {}",userId);
        UserDto userDto = this.modelMapper.map(user, UserDto.class);
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
        UserDto userDto = this.modelMapper.map(user, UserDto.class);
        logger.info("getUserByEmail service execution started");
        return userDto;
    }
    /**
     * @implNote get user by email and password
     */
    @Override
    public UserDto getUserByEmailAndPassword(String email, String password) {
        logger.info("getUserByEmailAndPassword service execution started");
        User user = this.userRepository.findByEmailAndPassword(email, password).orElseThrow(() -> new ResourceNotFoundException("User", "Email", email));
        logger.info("User found successfully for email : {}", email);
        UserDto userDto = this.modelMapper.map(user, UserDto.class);
        logger.info("getUserByEmailAndPassword service execution ended");
        return userDto;
    }
    /**
     * @implNote search user by keyword
     */
    @Override
    public PageResponse<UserDto> byNameContaining(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        logger.info("getUserByKeyword service execution started");
        try{
            Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            //@implNote pageNumber default starts from 0
            Pageable pageable = PageRequest.of(pageNumber-1, pageSize, sort);
            logger.info("get Pageable object with pageNumber {}, pageSize {}", pageNumber, pageSize);
            Page<User> userPage = this.userRepository.findByNameContaining(keyword, pageable);
            logger.info("get Page object for User");
            PageResponse<UserDto> pageResponse = PageHelper.getPageResponse(userPage, UserDto.class);
            logger.info("get PageResponse<UserDto> process successfully");
            logger.info("getUserByKeyword service execution ended");
            return pageResponse;
        }catch (RuntimeException ex){
            logger.info("IllegalArgumentException encounter");
            throw new IllegalArgumentsException(AppConstants.PAGE_ERROR_MSG);
        }
    }

    /**
     * @implNote builder design pattern to build User and UseDto objects
     */
//    public User userDtoToEntity(UserDto userDto) {
//        User user = User.builder()
//                .name(userDto.getName())
//                .email(userDto.getEmail().trim())
//                .password(userDto.getPassword().trim())
//                .about(userDto.getAbout())
//                .gender(userDto.getGender())
//                .image(userDto.getImage())
//                .roles(userDto.getRoles())
//                .build();
//        return user;
//    }
//    public UserDto entityToUserDto(User user) {
//        UserDto userDto = UserDto.builder()
//                .userId(user.getId())
//                .name(user.getName())
//                .email(user.getEmail())
//                .password(user.getPassword())
//                .about(user.getAbout())
//                .gender(user.getGender())
//                .image(user.getImage())
//                .roles(user.getRoles())
//                .build();
//        return userDto;
//    }
}
