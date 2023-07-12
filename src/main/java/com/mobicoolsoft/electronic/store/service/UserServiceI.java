package com.mobicoolsoft.electronic.store.service;

import com.mobicoolsoft.electronic.store.dto.UserDto;
import com.mobicoolsoft.electronic.store.dto.PageResponse;

import java.util.List;

public interface UserServiceI {

    /**
     * create new user
     */
    public UserDto createUser(UserDto userDto);

    /**
     * update individual user
     */
    public UserDto updateUser(UserDto userDto, String userId);

    /**
     * delete user by id
     */
    public void deleteUser(String userId);

    /**
     * get all users
     */
    public PageResponse<UserDto> getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    /**
     * get user by id
     */
    public UserDto getUserById(String userId);

    /**
     *  get user by email
     */
    public UserDto getUserByEmail(String email);

    /**
     *  get user by emai and password
     */
    public UserDto getUserByEmailAndPassword(String emaill, String password);

    /**
     *  search user
     */
    public List<UserDto> byNameContaining(String keyword);
}
