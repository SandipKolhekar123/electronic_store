package com.mobicoolsoft.electronic.store.controller;

import com.mobicoolsoft.electronic.store.config.AppConstants;
import com.mobicoolsoft.electronic.store.dto.*;
import com.mobicoolsoft.electronic.store.exception.FileNotAvailableException;
import com.mobicoolsoft.electronic.store.service.CategoryServiceI;
import com.mobicoolsoft.electronic.store.service.FileServiceI;
import com.mobicoolsoft.electronic.store.service.ProductServiceI;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author Sandip Kolhekar
 * @apiNote build rest controller layer for category services
 * @since 2022
 */
@RestController
@RequestMapping(AppConstants.CATEGORY_URL)
public class CategoryController {

    private final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryServiceI categoryServiceI;

    @Autowired
    private FileServiceI fileServiceI;

    @Autowired
    private ProductServiceI productServiceI;

    @Value("${category.profile.image.path}")
    private String imagePath;

    /**
     * @implNote create product for specified category
     * @param productDto
     * @param categoryId
     * @return a productDto
     */
    @PostMapping("/{categoryId}/products")
    public ResponseEntity<ProductDto> createProductWithCategory(@Valid @RequestBody ProductDto productDto, @PathVariable String categoryId) {
        logger.info("Api createProductWithCategory request started");
        ProductDto product = this.productServiceI.createProductWithCategory(productDto, categoryId);
        logger.info("Api createProductWithCategory request ended with response : {}", HttpStatus.CREATED);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    /**
     * @implNote create a category record
     * @param categoryDto
     * @return newly created category record
     */
    @PostMapping("/")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        logger.info("Api createNewUser request started");
        CategoryDto category = this.categoryServiceI.createCategory(categoryDto);
        logger.info("Api createNewUser request ended with response : {}", HttpStatus.CREATED);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    /**
     * @implNote assign category to the particular product
     * @param categoryId
     * @param productId
     * @return a productDto
     */
    @PutMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<ProductDto> assignCategoryToProduct(@PathVariable String categoryId, @PathVariable String productId){
        ProductDto productDto = this.productServiceI.assignCategoryToProduct(categoryId, productId);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    /**
     * @implNote get products of category by categoryId
     * @param categoryId
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return pageResponse
     */
    @GetMapping("/{categoryId}/products")
    public ResponseEntity<PageResponse<ProductDto>> getProductsWithCategory( @PathVariable String categoryId,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCT_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir
    ){
        PageResponse<ProductDto> pageResponse = this.productServiceI.getProductsByCategory(categoryId, pageNumber, pageSize, sortBy, sortDir);
        return  new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    /**
     * @implNote update category by categoryId
     * @param categoryDto
     * @param categoryId
     * @return updated category
     */
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable String categoryId) {
        logger.info("Api updateUser request started for user with userId : {}", categoryId);
        CategoryDto updateCategory = this.categoryServiceI.updateCategory(categoryDto, categoryId);
        logger.info("Api updateUser request ended with response : {}", HttpStatus.OK);
        return new ResponseEntity<>(updateCategory, HttpStatus.OK);
    }

    /**
     * @implNote delete category by categoryId
     * @param categoryId
     * @return ApiResponseMessage
     */
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId) {
        logger.info("Api deleteUser request for single user with userId : {}", categoryId);
        this.categoryServiceI.deleteCategory(categoryId);
        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
                .message("Category" + AppConstants.DELETE_MSG)
                .success(true)
                .status(HttpStatus.OK).build();
        logger.info("Api deleteUser request ended with response : {}", HttpStatus.OK);
        return new ResponseEntity<>(apiResponseMessage, HttpStatus.OK);
    }

    /**
     * @implNote get all category records
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return PageResponse containing list of CategoryDto
     */
    @GetMapping("/")
    public ResponseEntity<PageResponse<CategoryDto>> getAllCategories(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORY_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir)
    {
        logger.info("Api getAllUsers request started");
        PageResponse<CategoryDto> pageResponse = this.categoryServiceI.getAllCategories(pageNumber, pageSize, sortBy, sortDir);
        logger.info("Api getAllUsers request ended with response : {}", HttpStatus.OK);
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    /**
     * @implNote get category for categoryId
     * @param categoryId
     * @return a category
     */
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable String categoryId) {
        logger.info("Api getUserById request for User with userId  : {}", categoryId);
        CategoryDto categoryDto = this.categoryServiceI.getCategoryById(categoryId);
        logger.info("Api getUserById request ended with response : {}", HttpStatus.OK);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    /**
     * @implNote search category by keyword
     * @param keyword
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return PageResponse containing list of matched CategoryDto
     */
    @GetMapping("/search/{keyword}")
    public ResponseEntity<PageResponse<CategoryDto>> searchCategoryByKeyword(@PathVariable String keyword,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORY_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir)
    {
        logger.info("Api getUserByNameContaining request for User with keyword : {}", keyword);
        PageResponse<CategoryDto> pageResponse = this.categoryServiceI.searchCategoryByTitleKeyword(keyword, pageNumber, pageSize, sortBy, sortDir);
        logger.info("Api getUserByNameContaining request ended with response : {}", HttpStatus.OK);
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    /**
     * @implNote upload category image by categoryId
     * @param categoryImage
     * @param categoryId
     * @return ImageResponse
     * @throws IOException
     */
    @PostMapping("/upload/images/{categoryId}")
    public ResponseEntity<ImageResponse> uploadCategoryImage(
            @RequestParam("categoryImage") MultipartFile categoryImage,
            @PathVariable String categoryId) throws IOException
    {
        logger.info("Api uploadUserImage request with image : {}", categoryImage);
        CategoryDto categoryDto = this.categoryServiceI.getCategoryById(categoryId);
        logger.info("user found with userId : {}", categoryId);
        String uploadCategoryImage = this.fileServiceI.uploadImage(imagePath, categoryImage);
        logger.info("user image successfully upload on server!");
        categoryDto.setCoverImage(uploadCategoryImage);
        this.categoryServiceI.updateCategory(categoryDto, categoryId);
        logger.info("user image successfully saved in the database!");
        ImageResponse imageResponse = ImageResponse.builder()
                .imageName(uploadCategoryImage).message("Category" + AppConstants.IMAGE_MSG)
                .success(true).status(HttpStatus.CREATED).build();
        logger.info("Api uploadUserImage request ended with response : {}", HttpStatus.CREATED);
        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
    }

    /**
     * @implNote serve category image by userId
     * @param categoryId
     * @param response
     * @throws FileNotAvailableException
     */
    @GetMapping("/images/{categoryId}")
    public void serveCategoryImage(@PathVariable String categoryId, HttpServletResponse response) throws FileNotAvailableException {
        logger.info("Api serveUserImage request started with input response : {}", response);
        CategoryDto categoryDto = this.categoryServiceI.getCategoryById(categoryId);
        logger.info("get user image {}", categoryDto.getCoverImage());
        try {
            InputStream serveImage = this.fileServiceI.serveImage(imagePath, categoryDto.getCoverImage());
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            StreamUtils.copy(serveImage, response.getOutputStream());
        } catch (IOException ex) {
            logger.info("FileNotFoundException encounter");
            throw new FileNotAvailableException(categoryId);
        }
        logger.info("Api serveUserImage request ended with input response : {}", response);
    }
}
