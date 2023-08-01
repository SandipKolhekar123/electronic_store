package com.mobicoolsoft.electronic.store.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobicoolsoft.electronic.store.dto.PageResponse;
import com.mobicoolsoft.electronic.store.dto.UserDto;
import com.mobicoolsoft.electronic.store.entity.Role;
import com.mobicoolsoft.electronic.store.entity.User;
import com.mobicoolsoft.electronic.store.service.impl.UserServiceImpl;
import org.apache.el.stream.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest()
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Mock
    private UserServiceImpl userServiceImpl;

    @InjectMocks
    private UserController userController;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private PageResponse<UserDto> pageResponse;

    User user;

    Role role;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

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

    private String convertObjectToJsonString(User user) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(user);
    }

    @Test
    public void createNewUserTest() throws Exception {

        UserDto userDto = modelMapper.map(user, UserDto.class);

        when(userServiceImpl.createUser(Mockito.any())).thenReturn(userDto);


        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/api/users/")
                        .content(convertObjectToJsonString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").exists())
                .andDo(print());
    }



    @Test
    public void updateUserTest() throws Exception {

        UserDto userDto = modelMapper.map(user, UserDto.class);

        Mockito.when(userServiceImpl.updateUser(Mockito.any(),Mockito.anyString())).thenReturn(userDto);

        String userId = "suerIdTest";

        this.mockMvc.perform(
                        MockMvcRequestBuilders.put("/api/users/" + userId)
//                              .header(HttpHeaders.AUTHORIZATION, "Bearer dasjkklfsdfjksfsklfnklsnk;l;lv;lv;lv")
                                .content(convertObjectToJsonString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists())
                .andDo(print());
    }

    @Test
    @Disabled
    public void getAllUsersTest() throws Exception {
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

        List<Object> users = Arrays.asList(user, user1, user2);
        List<Object> userDtos = users.stream().map((user) -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
        PageResponse pageResponse = PageResponse.builder()
                .content(userDtos)
                .pageNumber(1)
                .pageSize(1)
                .totalElements(3L)
                .totalPages(1)
                .lastPage(true)
                .build();
        Mockito.when(userServiceImpl.getAllUsers(1,1,"name", "asc")).thenReturn(pageResponse);

        this.mockMvc.perform(
              MockMvcRequestBuilders.get("/api/users/")
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void getUserByIdTest() throws Exception {
        UserDto userDto = modelMapper.map(user, UserDto.class);

        Mockito.when(userServiceImpl.getUserById("xywssasa")).thenReturn(userDto);
       String  userId = "xywssasa";
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/users/" +userId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(".name").value("sandip"))             // .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath(".email").value("sandip@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath(".about").value("System designer"))
                .andExpect(MockMvcResultMatchers.jsonPath(".gender").value("male"))
                .andExpect(MockMvcResultMatchers.jsonPath(".image").value("default.png"))
                .andDo(print());
    }
}