package com.mobicoolsoft.electronic.store.service.impl;

import com.mobicoolsoft.electronic.store.dto.PageResponse;
import com.mobicoolsoft.electronic.store.dto.UserDto;
import com.mobicoolsoft.electronic.store.entity.Role;
import com.mobicoolsoft.electronic.store.entity.User;
import com.mobicoolsoft.electronic.store.exception.ResourceNotFoundException;
import com.mobicoolsoft.electronic.store.repository.RoleRepository;
import com.mobicoolsoft.electronic.store.repository.UserRepository;
import com.mobicoolsoft.electronic.store.service.UserServiceI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;
import java.util.function.BooleanSupplier;

@SpringBootTest(classes = UserServiceImplTest.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Spy
    private ModelMapper modelMapper;

    @Mock
    PageResponse<UserDto> pageResponse;

    User user;

    Role role;

    @BeforeEach
    public void setUpBeforeEach() {

        role = Role.builder().roleId(101).roleName("ADMIN").build();

        user = User.builder()
                .name("sandip")
                .email("sandip@gmail.com")
                .password("Sandip@123")
                .about("System designer")
                .gender("male")
                .image("default.png")
                .roles(Set.of(role))
                .build();
    }

    @Test
    public void createUserTest() {

        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
        Mockito.when(roleRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(role));

        UserDto userDto = userServiceImpl.createUser(modelMapper.map(user, UserDto.class));
        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(user.getName(), userDto.getName(), "expected user name does not match with actual user name!");
    }

    @Test
    void updateUserTest() {

        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);

        user.setId("abcd");
        UserDto userDto = userServiceImpl.updateUser(modelMapper.map(user, UserDto.class), user.getId());

        System.out.println(userDto.getName());

        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(user.getName(), userDto.getName());

    }

    @Test
    void deleteUserTest() {
        String userId = "userIdTest";
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userServiceImpl.deleteUser(userId);

        Mockito.verify(userRepository, Mockito.times(1)).delete(user);

//       Assertions.assertThrows(ResourceNotFoundException.class, () -> list.remove(1));
    }

    @Test
    void getAllUsersTest() {
        User user1 = User.builder()
                .name("jack dorsey")
                .email("jack@gmail.com")
                .password("Jack@123")
                .about("web developer")
                .gender("male")
                .image("default.png")
                .roles(Set.of(role))
                .build();
        User user2 = User.builder()
                .name("mark zuckerbuck")
                .email("mark@gmail.com")
                .password("Mark@123")
                .about("Businessman")
                .gender("male")
                .image("default.png")
                .roles(Set.of(role))
                .build();

        List<User> userList = Arrays.asList(user, user1, user2);
        Page<User> page = new PageImpl<>(userList);
        Mockito.when(userRepository.findAll((Pageable) Mockito.any())).thenReturn(page);

        PageResponse<UserDto> users = userServiceImpl.getAllUsers(1, 1, "name", "desc");

        Assertions.assertEquals(3, users.getContent().size());
    }

    @Test
    void getUserByIdTest() {

        user.setId("abcd");

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserDto userDto = userServiceImpl.getUserById(user.getId());

        System.out.println(userDto.getName());
        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(user.getId(), userDto.getUserId());
    }

    @Test
    void getUserByEmailTest() {

        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        UserDto userDto = userServiceImpl.getUserByEmail(user.getEmail());

        Assertions.assertEquals(user.getEmail(), userDto.getEmail());
    }

    @Test
    void getUserByEmailAndPasswordTest() {

        Mockito.when(userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(Optional.of(user));

        String email = "sandip@gmail.com";
        String password = "Sandip@123";
        UserDto userDto = userServiceImpl.getUserByEmailAndPassword(email, password);

        Assertions.assertEquals(user.getName(), userDto.getName());
    }

    @Test
    void byNameContainingTest() {
        User user1 = User.builder()
                .name("jack kolhekar")
                .email("jack@gmail.com")
                .password("Jack@123")
                .about("web developer")
                .gender("male")
                .image("default.png")
                .roles(Set.of(role))
                .build();
        User user2 = User.builder()
                .name("mark kolhekar")
                .email("mark@gmail.com")
                .password("Mark@123")
                .about("Businessman")
                .gender("male")
                .image("default.png")
                .roles(Set.of(role))
                .build();

        List<User> userList = Arrays.asList(user, user1, user2);
        Page<User> page = new PageImpl<>(userList);
        Mockito.when(userRepository.findByNameContaining(Mockito.anyString(), (Pageable) Mockito.any())).thenReturn(page);

        PageResponse<UserDto> users = userServiceImpl.byNameContaining("jack", 1, 1, "name", "desc");

        Assertions.assertEquals(3, users.getContent().size());
    }

}