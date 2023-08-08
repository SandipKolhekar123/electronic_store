package com.mobicoolsoft.electronic.store.service.impl;

import com.mobicoolsoft.electronic.store.dto.PageResponse;
import com.mobicoolsoft.electronic.store.dto.ProductDto;
import com.mobicoolsoft.electronic.store.entity.Category;
import com.mobicoolsoft.electronic.store.entity.Product;
import com.mobicoolsoft.electronic.store.repository.CategoryRepository;
import com.mobicoolsoft.electronic.store.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ProductServiceImplTest.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Spy
    private ModelMapper modelMapper;

    Product product;

    Category category;

    @BeforeEach
    public void beforeSetUp() {

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

    @Test
    void createProductTest() {
        Mockito.when(productRepository.save(Mockito.any())).thenReturn(product);

        ProductDto productDto = productService.createProduct(modelMapper.map(product, ProductDto.class));

        Assertions.assertNotNull(productDto);
        Assertions.assertEquals(product.getTitle(), productDto.getTitle());
    }

    @Test
    void createProductWithCategoryTest() {
        category = Category.builder()
                .title("Beauty Products")
                .description("ayurvedic beauty products")
                .coverImage("xyz.png")
                .products(Set.of(product))
                .build();

        product.setCategory(category);

        Mockito.when(categoryRepository.findById("wxyz")).thenReturn(Optional.of(category));
        Mockito.when(productRepository.save(Mockito.any())).thenReturn(product);
        String categoryId = "wxyz";

        ProductDto productDto = productService.createProductWithCategory(modelMapper.map(product, ProductDto.class), categoryId);
        System.out.println(productDto.getCategory().getTitle());
        Assertions.assertEquals(product.getTitle(), productDto.getTitle());
        Assertions.assertEquals(category.getTitle(), productDto.getCategory().getTitle());
    }

    @Test
    void assignCategoryToProductTest() {
        category = Category.builder()
                .title("Beuty Products")
                .description("ayurvedic beuty products")
                .coverImage("xyz.png")
                .products(Set.of(product))
                .build();

        String categoryId = "wxyz";
        String productId = "abcd";

        Mockito.when(categoryRepository.findById("wxyz")).thenReturn(Optional.of(category));
        Mockito.when(productRepository.findById("abcd")).thenReturn(Optional.of(product));
        Mockito.when(productRepository.save(Mockito.any())).thenReturn(product);


        ProductDto productDto = productService.assignCategoryToProduct(categoryId, productId);

        Assertions.assertEquals(product.getTitle(), productDto.getTitle());
        Assertions.assertEquals(category.getTitle(), productDto.getCategory().getTitle());
    }

    @Test
    void updateProductTest() {

        Mockito.when(productRepository.findById("abcd")).thenReturn(Optional.of(product));
        Mockito.when(productRepository.save(Mockito.any())).thenReturn(product);

        String productId = "abcd";

        ProductDto productDto = productService.updateProduct(modelMapper.map(product, ProductDto.class), productId);

        Assertions.assertNotNull(productDto);
        Assertions.assertEquals(product.getDescription(), productDto.getDescription());

    }

    @Test
    void deleteProductTest() {
        Mockito.when(productRepository.findById("abcd")).thenReturn(Optional.of(product));

        String productId = "abcd";

        productService.deleteProduct(productId);

        Mockito.verify(productRepository, Mockito.times(1)).delete(product);
    }

    @Test
    void getAllProductsTest() {
        Product product1 = Product.builder()
                .title("godrej")
                .description("body lotion")
                .price(190.00)
                .discount(10)
                .stock(true)
                .live(true)
                .quantity(150)
                .image("lotion.png")
                .build();

        Product product2 = Product.builder()
                .title("Nyayaka")
                .description("skin powder")
                .price(110.00)
                .discount(20)
                .stock(true)
                .live(false)
                .quantity(10)
                .image("default.png")
                .build();

        List<Product> products = Arrays.asList(product, product1, product2);
        Page<Product> page = new PageImpl(products);
        Mockito.when(productRepository.findAll((Pageable) Mockito.any())).thenReturn(page);

        PageResponse<ProductDto> allProducts = productService.getAllProducts(1, 1, "title", "Asc");

        Assertions.assertEquals(3, allProducts.getContent().size());
    }

    @Test
    void getProductByIdTest() {
        Mockito.when(productRepository.findById("abcd")).thenReturn(Optional.of(product));
        String productId = "abcd";
        ProductDto productDto = productService.getProductById(productId);

        Assertions.assertNotNull(productDto);
        Assertions.assertEquals(product.getTitle(), productDto.getTitle());
    }

    @Test
    void getProductsByCategoryTest() {
        Product product1 = Product.builder()
                .title("godrej")
                .description("body lotion")
                .price(190.00)
                .discount(10)
                .stock(true)
                .live(true)
                .quantity(150)
                .image("lotion.png")
                .build();

        Product product2 = Product.builder()
                .title("Nyayaka")
                .description("skin powder")
                .price(110.00)
                .discount(20)
                .stock(true)
                .live(false)
                .quantity(10)
                .image("default.png")
                .build();

        category = Category.builder()
                .title("Beuty Products")
                .description("ayurvedic beuty products")
                .coverImage("xyz.png")
                .products(Set.of(product))
                .build();

        List<Product> products = Arrays.asList(product, product1, product2);
        Page<Product> page = new PageImpl(products);
        Mockito.when(categoryRepository.findById("abcd")).thenReturn(Optional.of(category));
        Mockito.when(productRepository.findByCategory((Category) Mockito.any(), (Pageable) Mockito.any())).thenReturn(page);

        String categoryId = "abcd";
        PageResponse<ProductDto> response = productService.getProductsByCategory(categoryId, 1, 5, "title", "asc");

        System.out.println(response.getContent().size());
        System.out.println(response.getTotalElements());
        Assertions.assertEquals(3, response.getContent().size());
        Assertions.assertEquals(3, response.getTotalElements());
        Assertions.assertTrue(response.getLastPage());
    }

    @Test
//  @Disabled
    void getByTitleContainingTest() {
        Product product1 = Product.builder()
                .title("godrej")
                .description("body lotion")
                .price(190.00)
                .discount(10)
                .stock(true)
                .live(true)
                .quantity(150)
                .image("lotion.png")
                .build();

        Product product2 = Product.builder()
                .title("Nyayaka")
                .description("skin powder")
                .price(110.00)
                .discount(20)
                .stock(true)
                .live(false)
                .quantity(10)
                .image("default.png")
                .build();

        List<Product> products = Arrays.asList(product, product1, product2);
        Page<Product> page = new PageImpl(products);
        Mockito.when(productRepository.findByTitleContaining(Mockito.anyString(), (Pageable) Mockito.any())).thenReturn(page);

        PageResponse<ProductDto> response = productService.getByTitleContaining("godrej", 1, 1, "title", "asc");

        Assertions.assertEquals(3, response.getContent().size());
    }

    @Test
    void getByLiveTrueTest() {
        Product product1 = Product.builder()
                .title("godrej")
                .description("body lotion")
                .price(190.00)
                .discount(10)
                .stock(true)
                .live(true)
                .quantity(150)
                .image("lotion.png")
                .build();

        Product product2 = Product.builder()
                .title("Nyayaka")
                .description("skin powder")
                .price(110.00)
                .discount(20)
                .stock(true)
                .live(true)
                .quantity(10)
                .image("default.png")
                .build();

        List<Product> products = Arrays.asList(product, product1, product2);
        Page<Product> page = new PageImpl(products);

        Mockito.when(productRepository.findByLiveTrue((Pageable) Mockito.any())).thenReturn(page);

        PageResponse<ProductDto> response = productService.getByLiveTrue(1, 1, "rirle", "ace");

        Assertions.assertEquals(3, response.getContent().size());
    }
}