package com.mobicoolsoft.electronic.store.service.impl;

import com.mobicoolsoft.electronic.store.dto.CategoryDto;
import com.mobicoolsoft.electronic.store.dto.PageResponse;
import com.mobicoolsoft.electronic.store.entity.Category;
import com.mobicoolsoft.electronic.store.entity.Product;
import com.mobicoolsoft.electronic.store.repository.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CategoryServiceImplTest.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Spy
    private ModelMapper modelMapper;

    Category category;

    Product product;

    @BeforeEach
    public void init() {
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

    @Test
    void createCategoryTest() {
        Mockito.when(categoryRepository.save(Mockito.any())).thenReturn(category);

        CategoryDto categoryDto = this.categoryService.createCategory(modelMapper.map(category, CategoryDto.class));

        Assertions.assertNotNull(categoryDto);
        Assertions.assertEquals(category.getTitle(), categoryDto.getTitle());
    }

    @Test
    void updateCategoryTest() {
        Mockito.when(categoryRepository.findById("sandsds")).thenReturn(Optional.of(category));
        Mockito.when(categoryRepository.save(Mockito.any())).thenReturn(category);

        String categoryId = "sandsds";

        CategoryDto categoryDto = this.categoryService.updateCategory(modelMapper.map(category, CategoryDto.class), categoryId);

        Assertions.assertNotNull(categoryDto);
        Assertions.assertEquals(category.getTitle(), categoryDto.getTitle());
    }

    @Test
    void deleteCategoryTest() {
        Mockito.when(categoryRepository.findById("catIdTest")).thenReturn(Optional.of(category));
        String categoryId = "catIdTest";
        this.categoryService.deleteCategory(categoryId);

        Mockito.verify(categoryRepository, Mockito.times(1)).delete(category);
    }

    @Test
    void getAllCategoriesTest() {
        Category category1 = Category.builder()
                .title("Beauty Products")
                .description("ayurvedic beauty products")
                .coverImage("xyz.png")
                .products(Set.of(product))
                .build();
        Category category2 = Category.builder()
                .title("Apparels")
                .description("ducky pants")
                .coverImage("xyz.png")
                .products(Set.of(product))
                .build();

        List<Category> categories = Arrays.asList(category, category1, category2);
        Page<Category> page = new PageImpl<>(categories);

        Mockito.when(categoryRepository.findAll((Pageable) Mockito.any())).thenReturn(page);

        PageResponse<CategoryDto> allCategories = this.categoryService.getAllCategories(1, 1, "title", "desc");

        Assertions.assertEquals(3, allCategories.getContent().size());
    }

    @Test
    void getCategoryByIdTest() {
        Mockito.when(categoryRepository.findById("catIdTest")).thenReturn(Optional.of(category));

        String categoryId = "catIdTest";
        CategoryDto categoryDto = this.categoryService.getCategoryById(categoryId);

        Assertions.assertEquals(category.getTitle(), categoryDto.getTitle());
    }

    @Test
    void searchCategoryByTitleKeywordTest() {

        Category category1 = Category.builder()
                .title("Beauty Products")
                .description("ayurvedic beauty products")
                .coverImage("xyz.png")
                .products(Set.of(product))
                .build();
        Category category2 = Category.builder()
                .title("Apparels")
                .description("ducky pants")
                .coverImage("xyz.png")
                .products(Set.of(product))
                .build();

        List<Category> categories = Arrays.asList(category, category1, category2);
        Page<Category> page = new PageImpl<>(categories);

        Mockito.when(categoryRepository.findByTitleContaining(Mockito.anyString(), (Pageable) Mockito.any())).thenReturn(page);

        PageResponse<CategoryDto> response = this.categoryService.searchCategoryByTitleKeyword("Beauty", 1, 1, "title", "asc");

        Assertions.assertEquals(3, response.getContent().size());

    }
}