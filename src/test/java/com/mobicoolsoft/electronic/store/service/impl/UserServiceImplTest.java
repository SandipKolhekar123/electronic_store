package com.mobicoolsoft.electronic.store.service.impl;
import com.mobicoolsoft.electronic.store.dto.UserDto;
import com.mobicoolsoft.electronic.store.entity.User;
import com.mobicoolsoft.electronic.store.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = UserServiceImplTest.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUserTest() {

        UserDto userDto = new UserDto("H101", "sandip", "sandip@gmail.com", "Sandip@123", "male", "i am developer", "default.jpg");

        User user = userService.userDtoToEntity(userDto);

        when(userRepository.save(user)).thenReturn(user);

        UserDto userDto1 = userService.createUser(userDto);

        System.out.println(userDto);
        System.out.println(userDto1);
        assertEquals(userDto.getUserId(), userDto1.getUserId());
    }

    @Test
    void updateUserTest() {
    }

    @Test
    void deleteUserTest() {
    }

    @Test
    void getAllUsersTest() {
    }

    @Test
    void getUserByIdTest() {
    }

    @Test
    void getUserByEmailTest() {
    }

    @Test
    void getUserByEmailAndPasswordTest() {
    }

    @Test
    void byNameContainingTest() {

    }
}