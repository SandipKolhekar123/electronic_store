package com.mobicoolsoft.electronic.store.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobicoolsoft.electronic.store.dto.ApiResponseMessage;
import com.mobicoolsoft.electronic.store.dto.PageResponse;
import com.mobicoolsoft.electronic.store.dto.UserDto;
import com.mobicoolsoft.electronic.store.entity.Role;
import com.mobicoolsoft.electronic.store.entity.User;
import com.mobicoolsoft.electronic.store.service.impl.FileServiceImpl;
import com.mobicoolsoft.electronic.store.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.el.stream.Optional;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.matches;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(classes = UserControllerTest.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Mock
    private UserServiceImpl userServiceImpl;

    @InjectMocks
    private UserController userController;

    @Spy
    private ModelMapper modelMapper;

    @Mock
    private FileServiceImpl fileService;

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

        Mockito.when(userServiceImpl.updateUser(Mockito.any(), Mockito.anyString())).thenReturn(userDto);

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
//delete User test case
    @Test
    public void deleteUserTest() throws Exception {
        user.setId("userIdTest");
        String userId = "userIdTest";
        ResponseEntity<ApiResponseMessage> response = this.userController.deleteUser(userId);
        int expectedStatus = 200;
        int actualStatus = response.getStatusCode().value();
        Assertions.assertEquals(expectedStatus, actualStatus);
    }

    @Test
//  @Disabled
    public void getAllUsersTest() throws Exception {

        User user1 = User.builder()
                .name("palak")
                .email("palak@gmail.com")
                .password("Palak@123")
                .about("System designer")
                .gender("female")
                .image("default.png")
                .roles(Set.of(role))
                .build();

        User user2 = User.builder()
                .name("manish")
                .email("manish@gmail.com")
                .password("Manish@123")
                .about("System designer")
                .gender("male")
                .image("default.png")
                .roles(Set.of(role))
                .build();

        List<User> userList = Arrays.asList(user, user1, user2);
        List<UserDto> userDtos = userList.stream().map((user) -> this.modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
        Integer pageNumber = 0;
        Integer pageSize = 2;
        Long totalElements = 15l;
        Integer totalSize = 5;
        Boolean lastPage = false;
        String sortBy = "name";
        String sortDir = "asc";
        PageResponse pageResponse = new PageResponse(userDtos, pageNumber, pageSize, totalElements, totalSize, lastPage);

        Mockito.when(userServiceImpl.getAllUsers(pageNumber, pageSize, sortBy, sortDir)).thenReturn(pageResponse);

        ResponseEntity<PageResponse<UserDto>> response = userController.getAllUsers(pageNumber, pageSize, sortBy, sortDir);

        int expectedSize = 3;
        int actualSize = response.getBody().getContent().size();

        Assertions.assertEquals(expectedSize, actualSize);
    }

    @Test
    public void getUserByIdTest() throws Exception {
        UserDto userDto = modelMapper.map(user, UserDto.class);

        Mockito.when(userServiceImpl.getUserById("xywssasa")).thenReturn(userDto);
        String userId = "xywssasa";
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/users/" + userId)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(".name").value("sandip"))             // .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath(".email").value("sandip@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath(".about").value("System designer"))
                .andExpect(MockMvcResultMatchers.jsonPath(".gender").value("male"))
                .andExpect(MockMvcResultMatchers.jsonPath(".image").value("default.png"))
                .andDo(print());
    }

    @Test
    public void getUserByEmailTest() throws Exception {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        Mockito.when(userServiceImpl.getUserByEmail(Mockito.anyString())).thenReturn(userDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/users/email")
                                .param("email", "sandip@gmail.com")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(".name").value("sandip"))             // .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath(".email").value("sandip@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath(".about").value("System designer"))
                .andExpect(MockMvcResultMatchers.jsonPath(".gender").value("male"))
                .andExpect(MockMvcResultMatchers.jsonPath(".image").value("default.png"))
                .andDo(print());
    }

    @Test
    public void getUserByEmailAndPasswordTest() throws Exception {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        Mockito.when(userServiceImpl.getUserByEmailAndPassword(Mockito.anyString(), Mockito.anyString())).thenReturn(userDto);
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/users/user")
                                .param("email", "sandip@gmail.com")
                                .param("password", "Sandip@123")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(".name").value("sandip"))
                .andExpect(MockMvcResultMatchers.jsonPath(".email").value("sandip@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath(".about").value("System designer"))
                .andExpect(MockMvcResultMatchers.jsonPath(".gender").value("male"))
                .andExpect(MockMvcResultMatchers.jsonPath(".image").value("default.png"))
                .andDo(print());
    }

    @Test
    public void getByNameContaining() {
        User user1 = User.builder()
                .name("palak")
                .email("palak@gmail.com")
                .password("Palak@123")
                .about("System designer")
                .gender("female")
                .image("default.png")
                .roles(Set.of(role))
                .build();

        User user2 = User.builder()
                .name("manish")
                .email("manish@gmail.com")
                .password("Manish@123")
                .about("System designer")
                .gender("male")
                .image("default.png")
                .roles(Set.of(role))
                .build();

        List<User> userList = Arrays.asList(user, user1, user2);
        List<UserDto> userDtos = userList.stream().map((user) -> this.modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
        Integer pageNumber = 0;
        Integer pageSize = 2;
        Long totalElements = 15l;
        Integer totalSize = 5;
        Boolean lastPage = false;
        String sortBy = "name";
        String sortDir = "asc";
        PageResponse pageResponse = new PageResponse(userDtos, pageNumber, pageSize, totalElements, totalSize, lastPage);

        Mockito.when(userServiceImpl.byNameContaining("sandip", pageNumber, pageSize, sortBy, sortDir)).thenReturn(pageResponse);

        ResponseEntity<PageResponse<UserDto>> response = userController.byNameContaining("sandip", pageNumber, pageSize, sortBy, sortDir);

        int expectedSize = 3;
        int actualSize = response.getBody().getContent().size();

        int expectedStatus = 200;
        int actualStatus = response.getStatusCode().value();

        Assertions.assertEquals(expectedStatus, actualStatus);
        Assertions.assertEquals(expectedSize, actualSize);
    }

    @Test
    public void uploadUserImageTest() throws Exception {
//        UserDto userDto = modelMapper.map(user, UserDto.class);
//        Mockito.when(userServiceImpl.getUserById(Mockito.anyString())).thenReturn(userDto);
//        Mockito.when(fileService.uploadImage(Mockito.anyString(), (MultipartFile) Mockito.any())).thenReturn(Mockito.anyString());

        String userId = "asdfg";

        MockMultipartFile mockMultipartFile = new MockMultipartFile("user-file", "sandy.png",
                "text/plain", "test_data".getBytes());

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/api/users/images/upload/" + userId)
                        .file(mockMultipartFile);

//        File f = new File("C:\\Users\\HP\\OneDrive\\Desktop\\localdisk\\IdeaProjects\\ElectronicStore\\images\\users");
//        FileInputStream fis = new FileInputStream(f);
//        System.out.println(f.isFile()+"  "+f.getName()+f.exists());
//        MockMultipartFile mockMultipartFile = new MockMultipartFile("upload", "Tulips.jpg","multipart/form-data", fis);
//        this.mockMvc.perform(
//                        MockMvcRequestBuilders.post("/api/users/images/upload/" + userId)
//                                .param("userImage", "user_image.jpeg")
//                                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
//                ).andExpect(status().isCreated())
//                .andDo(print());
    }

//    @Test
//    public void serveUserImageTest() throws Exception {
//        UserDto userDto = modelMapper.map(user, UserDto.class);
//        InputStream inputStream = new FileInputStream("default.png");
//        Mockito.when(fileService.serveImage(Mockito.anyString(), user.getImage())).thenReturn(inputStream);
//        user.setId("assasasa");
//        this.mockMvc.perform(
//                        MockMvcRequestBuilders.post("/api/users/image/" + user.getId())
//                ).andExpect(status().isCreated())
//                .andDo(print());
//    }
}
