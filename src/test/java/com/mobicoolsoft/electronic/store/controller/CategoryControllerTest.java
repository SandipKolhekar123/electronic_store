package com.mobicoolsoft.electronic.store.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobicoolsoft.electronic.store.dto.CategoryDto;
import com.mobicoolsoft.electronic.store.dto.PageResponse;
import com.mobicoolsoft.electronic.store.dto.ProductDto;
import com.mobicoolsoft.electronic.store.dto.UserDto;
import com.mobicoolsoft.electronic.store.entity.Category;
import com.mobicoolsoft.electronic.store.entity.Product;
import com.mobicoolsoft.electronic.store.entity.User;
import com.mobicoolsoft.electronic.store.repository.CategoryRepository;
import com.mobicoolsoft.electronic.store.repository.ProductRepository;
import com.mobicoolsoft.electronic.store.service.impl.CategoryServiceImpl;
import com.mobicoolsoft.electronic.store.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = CategoryControllerTest.class)
@AutoConfigureMockMvc
class CategoryControllerTest {

    @Mock
    private ProductServiceImpl productService;
    @Mock
    private CategoryServiceImpl categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @Spy
    private ModelMapper modelMapper;

    @Autowired
    MockMvc mockMvc;
    Product product;

    Category category;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();

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

        category = Category.builder()
                .title("Beauty Products")
                .description("ayurvedic beauty products")
                .coverImage("xyz.png")
                .products(Set.of(product))
                .build();
    }

    private String convertObjectToJsonString(Category category) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(category);
    }

    @Test
    void createProductWithCategoryTest() throws Exception {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        Mockito.when(productService.createProductWithCategory(Mockito.any(), Mockito.anyString())).thenReturn(productDto);

        String categoryId = "xyzABC";
        String productValueAsString = new ObjectMapper().writeValueAsString(productDto);
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/categories/{categoryId}/products", categoryId)
                                .content(productValueAsString)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").exists())
                .andDo(print());
    }

    @Test
    void createCategoryTest() throws Exception {
        CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);
        Mockito.when(categoryService.createCategory(Mockito.any())).thenReturn(categoryDto);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/categories/")
                                .content(convertObjectToJsonString(category))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").exists())
                .andDo(print());
    }

    @Test
    void updateCategoryTest() throws Exception {
        CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);
        Mockito.when(categoryService.updateCategory(Mockito.any(), Mockito.anyString())).thenReturn(categoryDto);

        String categoryId = "xyzABC";
        this.mockMvc.perform(
                        MockMvcRequestBuilders.put("/api/categories/" + categoryId)
                                .content(convertObjectToJsonString(category))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").exists())
                .andDo(print());
    }

    @Test
    void assignCategoryToProductTest() throws Exception {
        category.setId("catId");
        product.setCategory(category);
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        Mockito.when(productService.assignCategoryToProduct(Mockito.anyString(), Mockito.anyString())).thenReturn(productDto);
        String categoryId = "catId";
        String productId = "prodId";
        this.mockMvc.perform(
                        MockMvcRequestBuilders.put("/api/categories/{categoryId}/products/{productId}", categoryId, productId)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").exists())
                .andDo(print());
    }

    @Test
    void getProductWithCategoryTest() {
        Product product1 = Product.builder()
                .title("godrej")
                .description("skin face wash")
                .price(100.00)
                .discount(40)
                .stock(true)
                .live(false)
                .quantity(50)
                .image("default.png")
                .build();
        Product product2 = Product.builder()
                .title("godrej")
                .description("skin lotion")
                .price(1900.00)
                .discount(10)
                .stock(false)
                .live(false)
                .quantity(15)
                .image("default.png")
                .build();
        category.setId("categoryIdTest");
        product.setCategory(category);
        product1.setCategory(category);
        product2.setCategory(category);
        List<Product> products = Arrays.asList(product, product1, product2);
        List<ProductDto> productDtos = products.stream().map((user) -> this.modelMapper.map(products, ProductDto.class)).collect(Collectors.toList());
        Integer pageNumber = 1;
        Integer pageSize = 1;
        Long totalElements = 3l;
        Integer totalSize = 5;
        Boolean lastPage = false;
        String sortBy = "name";
        String sortDir = "asc";
        PageResponse pageResponse = new PageResponse(productDtos, pageNumber, pageSize, totalElements, totalSize, lastPage);

        Mockito.when(productService.getProductsByCategory("categoryIdTest", pageNumber, pageSize, sortBy, sortDir)).thenReturn(pageResponse);

        ResponseEntity<PageResponse<ProductDto>> response = this.categoryController.getProductsWithCategory(category.getId(), pageNumber, pageSize, sortBy, sortDir);

        int actualStatus = response.getStatusCode().value();
        int expectedStatus = 200;

        Assertions.assertEquals(expectedStatus, expectedStatus);
        Assertions.assertEquals(3, response.getBody().getContent().size());
    }

    @Test
    void deleteCategoryTest() throws Exception {
        category.setId("xyzId");
        CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);
        Mockito.when(categoryService.getCategoryById(Mockito.anyString())).thenReturn(categoryDto);
        Mockito.doNothing().when(categoryService).deleteCategory(category.getId());

        this.mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/categories/" + category.getId())
                ).andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getAllCategoriesTest() {
        Category category1 = Category.builder()
                .title("Sports Products")
                .description("sport t shirt")
                .coverImage("abc.png")
                .products(Set.of(product))
                .build();

        Category category2 = Category.builder()
                .title("Apparels")
                .description("women's cloth")
                .coverImage("abcd.png")
                .products(Set.of(product))
                .build();

        List<Category> categoryList = Arrays.asList(category, category1, category2);
        List<CategoryDto> categoryDtos = categoryList.stream().map((user) -> this.modelMapper.map(category, CategoryDto.class)).collect(Collectors.toList());
        Integer pageNumber = 0;
        Integer pageSize = 2;
        Long totalElements = 3l;
        Integer totalSize = 5;
        Boolean lastPage = false;
        String sortBy = "name";
        String sortDir = "asc";
        PageResponse pageResponse = new PageResponse(categoryDtos, pageNumber, pageSize, totalElements, totalSize, lastPage);

        Mockito.when(categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortDir)).thenReturn(pageResponse);

        ResponseEntity<PageResponse<CategoryDto>> response = categoryController.getAllCategories(pageNumber, pageSize, sortBy, sortDir);

        int expectedSize = 3;
        int actualSize = response.getBody().getContent().size();

        Assertions.assertEquals(expectedSize, actualSize);
    }

    @Test
    void getCategoryByIdTest() throws Exception {
        category.setId("dadassd");
        CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);
        Mockito.when(categoryService.getCategoryById(Mockito.anyString())).thenReturn(categoryDto);
        String categoryId = "dadassd";
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/categories/" + categoryId)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(".title").value("Beauty Products"))
                .andExpect(MockMvcResultMatchers.jsonPath(".description").value("ayurvedic beauty products"))
                .andExpect(MockMvcResultMatchers.jsonPath(".coverImage").value("xyz.png"))
                .andDo(print());
    }

    @Test
    void searchCategoryByKeywordTest() {
        Category category1 = Category.builder()
                .title("Sports Products")
                .description("sport t shirt")
                .coverImage("abc.png")
                .products(Set.of(product))
                .build();

        Category category2 = Category.builder()
                .title("Apparels")
                .description("women's cloth")
                .coverImage("abcd.png")
                .products(Set.of(product))
                .build();
        List<Category> categoryList = Arrays.asList(category, category1, category2);
        List<CategoryDto> categoryDtos = categoryList.stream().map((user) -> this.modelMapper.map(category, CategoryDto.class)).collect(Collectors.toList());
        Integer pageNumber = 0;
        Integer pageSize = 2;
        Long totalElements = 3l;
        Integer totalSize = 5;
        Boolean lastPage = false;
        String sortBy = "title";
        String sortDir = "asc";
        PageResponse pageResponse = new PageResponse(categoryDtos, pageNumber, pageSize, totalElements, totalSize, lastPage);
        Mockito.when(categoryService.searchCategoryByTitleKeyword("Aapparrels", pageNumber, pageSize, sortBy, sortDir)).thenReturn(pageResponse);

        ResponseEntity<PageResponse<CategoryDto>> response = categoryController.searchCategoryByKeyword("Aapparrels", pageNumber, pageSize, sortBy, sortDir);
        int expectedSize = 3;
        int actualSize = response.getBody().getContent().size();

        Assertions.assertEquals(expectedSize, actualSize);
    }

    @Test
    void uploadCategoryImageTest() {
    }

    @Test
    void serveCategoryImageTest() {
    }
}