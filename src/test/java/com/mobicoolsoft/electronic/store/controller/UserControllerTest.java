package com.mobicoolsoft.electronic.store.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobicoolsoft.electronic.store.dto.UserDto;
import com.mobicoolsoft.electronic.store.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import static org.hamcrest.CoreMatchers.is;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.ArrayList;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(classes = UserController.class)
@AutoConfigureMockMvc
@ContextConfiguration
@ComponentScan(basePackages = "com.mobicoolsoft.electronic.store")
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Mock
    private UserServiceImpl userServiceImpl;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void createNewUserTest() throws Exception {

        UserDto userDto = new UserDto("string", "sandip", "sandip@gmail.com", "Sandip@123", "male", "i am developer", "default.jpg");

        when(userServiceImpl.createUser(userDto)).thenReturn(userDto);

        ObjectMapper objectMapper = new ObjectMapper();

        String userString = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/api/users/")
                        .content(userString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    public void updateUserTest() throws Exception {

        UserDto userDto = new UserDto("string", "sandip", "sandip@gmail.com", "Sandip@123", "male", "i am developer", "default.jpg");

        when(userServiceImpl.updateUser(userDto, userDto.getUserId())).thenReturn(userDto);

        ObjectMapper objectMapper = new ObjectMapper();

        String userString = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(put("/api/users/{userId}", userDto.getUserId())
                        .content(userString)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andDo(print());
    }

    @Test
    public void getAllUsersTest() throws Exception {
        List<UserDto> userDtos = new ArrayList<>();
        userDtos.add(new UserDto("swk", "sandip", "sandip@gmail.com", "Sandip@123", "male", "i am developer", "default.jpg"));
        userDtos.add(new UserDto("mwk", "manish", "manish@gmail.com", "Manish@123", "male", "i am HR manager", "default.jpg"));
        userDtos.add(new UserDto("rwk", "palak", "palak@gmail.com", "Palak@123", "female", "i am doctor", "default.jpg"));

//        when(userServiceImpl.getAllUsers(Integer pageNumber, Integer pageSize)).thenReturn(userDtos);

        mockMvc.perform(get("/api/users/"))
                .andExpect(jsonPath("$.size()", is(userDtos.size())))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void getUserByIdTest() throws Exception {
        UserDto userDto = new UserDto("id11", "sandip", "sandip@gmail.com", "Sandip@123", "male", "i am developer", "default.jpg");

        when(userServiceImpl.getUserById(userDto.getUserId())).thenReturn(userDto);

        mockMvc.perform(get("/api/users/{id}", userDto.getUserId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(".userId").value("id11"))
                .andExpect(MockMvcResultMatchers.jsonPath(".name").value("sandip"))             // .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath(".email").value("sandip@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath(".password").value("Sandip@123"))
                .andExpect(MockMvcResultMatchers.jsonPath(".gender").value("male"))
                .andExpect(MockMvcResultMatchers.jsonPath(".about").value("i am developer"))
                .andExpect(MockMvcResultMatchers.jsonPath(".image").value("default.jpg"))
                .andDo(print());
    }
}