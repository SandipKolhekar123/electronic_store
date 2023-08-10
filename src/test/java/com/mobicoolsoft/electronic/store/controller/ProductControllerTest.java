package com.mobicoolsoft.electronic.store.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobicoolsoft.electronic.store.dto.PageResponse;
import com.mobicoolsoft.electronic.store.dto.ProductDto;
import com.mobicoolsoft.electronic.store.entity.Product;
import com.mobicoolsoft.electronic.store.repository.ProductRepository;
import com.mobicoolsoft.electronic.store.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ProductControllerTest.class)
@AutoConfigureMockMvc
class ProductControllerTest {

    @Mock
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductController productController;

    @Spy
    private ModelMapper modelMapper;

    Product product;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    public void init() {

        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();

        product = Product.builder()
                .title("godrej")
                .description("skin powder")
                .price(19000.00)
                .discount(20)
                .stock(true)
                .live(false)
                .quantity(150)
                .image("default.png")
                .build();
    }

    private String convertObjectToJsonString(Product product) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(product);
    }

    @Test
    void createProductTest() throws Exception {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        Mockito.when(productService.createProduct(Mockito.any())).thenReturn(productDto);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/products/")
                                .content(convertObjectToJsonString(product))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void updateProductTest() throws Exception {
        product.setId("productIdSet");
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        Mockito.when(productService.updateProduct(Mockito.any(), Mockito.anyString())).thenReturn(productDto);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.put("/api/products/" + product.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertObjectToJsonString(product))
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(".title").value("godrej"))             // .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath(".description").value("skin powder"))
                .andExpect(MockMvcResultMatchers.jsonPath(".price").value(19000.00))
                .andExpect(MockMvcResultMatchers.jsonPath(".discount").value(20))
                .andExpect(MockMvcResultMatchers.jsonPath(".live").value(false))
                .andDo(print());
    }

    @Test
    void deleteProductTest() throws Exception {
        product.setId("productIdSet");
        Mockito.when(productRepository.findById(Mockito.anyString())).thenReturn(Optional.of(product));
        Mockito.doNothing().when(productService).deleteProduct(product.getId());
        this.mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/products/" + product.getId())
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("message").value(" deleted successfully!"))
                .andDo(print());
    }

    @Test
    void getAllProductsTest() throws Exception {
        Product product1 = Product.builder()
                .title("saffron")
                .description("milk powder")
                .price(100000.00)
                .discount(5)
                .stock(false)
                .live(true)
                .quantity(150)
                .image("default.png")
                .build();

        Product product2 = Product.builder()
                .title("godrej")
                .description("skin powder")
                .price(19000.00)
                .discount(20)
                .stock(true)
                .live(false)
                .quantity(150)
                .image("default.png")
                .build();
        List<Product> productList = Arrays.asList(product, product1, product2);
        List<ProductDto> productDtos = productList.stream().map((product) -> modelMapper.map(product, ProductDto.class)).collect(Collectors.toList());
        Integer pageNumber = 0;
        Integer pageSize = 2;
        Long totalElements = 3l;
        Integer totalSize = 5;
        Boolean lastPage = false;
        String sortBy = "title";
        String sortDir = "asc";
        PageResponse pageResponse = new PageResponse(productDtos, pageNumber, pageSize, totalElements, totalSize, lastPage);
        Mockito.when(productService.getAllProducts(pageNumber, pageSize, sortBy, sortDir)).thenReturn(pageResponse);
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/products/")
                                .accept(MediaType.APPLICATION_JSON)
                                .param("pageNumber", "0")
                                .param("pageSize", "2")
                                .param("sortBy", "title")
                                .param("sortDir", "asc"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(6))
                .andDo(print());
    }

    @Test
    void getProductByIdTest() throws Exception {
        product.setId("productIdSet");
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        Mockito.when(productService.getProductById(Mockito.anyString())).thenReturn(productDto);
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/products/" + product.getId())
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(".title").value("godrej"))             // .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath(".description").value("skin powder"))
                .andExpect(MockMvcResultMatchers.jsonPath(".price").value(19000.00))
                .andExpect(MockMvcResultMatchers.jsonPath(".discount").value(20))
                .andExpect(MockMvcResultMatchers.jsonPath(".live").value(false))
                .andDo(print());
    }

    @Test
    void searchProductTest() throws Exception {
        Product product1 = Product.builder()
                .title("saffron")
                .description("milk powder")
                .price(100000.00)
                .discount(5)
                .stock(false)
                .live(true)
                .quantity(150)
                .image("default.png")
                .build();

        Product product2 = Product.builder()
                .title("godrej")
                .description("skin powder")
                .price(19000.00)
                .discount(20)
                .stock(true)
                .live(false)
                .quantity(150)
                .image("default.png")
                .build();
        List<Product> productList = Arrays.asList(product, product1, product2);
        List<ProductDto> productDtos = productList.stream().map((product) -> modelMapper.map(product, ProductDto.class)).collect(Collectors.toList());
        Integer pageNumber = 0;
        Integer pageSize = 2;
        Long totalElements = 3l;
        Integer totalSize = 5;
        Boolean lastPage = false;
        String sortBy = "title";
        String sortDir = "asc";
        PageResponse pageResponse = new PageResponse(productDtos, pageNumber, pageSize, totalElements, totalSize, lastPage);
        Mockito.when(productService.getByTitleContaining("godrej", pageNumber, pageSize, sortBy, sortDir)).thenReturn(pageResponse);
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/products/search/{query}", "godrej")
                                .accept(MediaType.APPLICATION_JSON)
                                .param("pageNumber", "0")
                                .param("pageSize", "2")
                                .param("sortBy", "title")
                                .param("sortDir", "asc"))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    void getAllLiveTest() throws Exception {
        Product product1 = Product.builder()
                .title("saffron")
                .description("milk powder")
                .price(100000.00)
                .discount(5)
                .stock(false)
                .live(true)
                .quantity(150)
                .image("default.png")
                .build();

        Product product2 = Product.builder()
                .title("godrej")
                .description("skin powder")
                .price(19000.00)
                .discount(20)
                .stock(true)
                .live(false)
                .quantity(150)
                .image("default.png")
                .build();
        List<Product> productList = Arrays.asList(product, product1, product2);
        List<ProductDto> productDtos = productList.stream().map((product) -> modelMapper.map(product, ProductDto.class)).collect(Collectors.toList());
        Integer pageNumber = 0;
        Integer pageSize = 2;
        Long totalElements = 3l;
        Integer totalSize = 5;
        Boolean lastPage = false;
        String sortBy = "title";
        String sortDir = "asc";
        PageResponse pageResponse = new PageResponse(productDtos, pageNumber, pageSize, totalElements, totalSize, lastPage);
        Mockito.when(productService.getByLiveTrue(pageNumber, pageSize, sortBy, sortDir)).thenReturn(pageResponse);
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/products/live")
                                .accept(MediaType.APPLICATION_JSON)
                                .param("pageNumber", "0")
                                .param("pageSize", "2")
                                .param("sortBy", "title")
                                .param("sortDir", "asc"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void uploadProductImageTest() {

    }

    @Test
    void serveProductImageTest() {

    }
}