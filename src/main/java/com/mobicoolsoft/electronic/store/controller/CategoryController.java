package com.mobicoolsoft.electronic.store.controller;

import com.mobicoolsoft.electronic.store.config.AppConstants;
import com.mobicoolsoft.electronic.store.dto.ApiResponseMessage;
import com.mobicoolsoft.electronic.store.dto.CategoryDto;
import com.mobicoolsoft.electronic.store.dto.ImageResponse;
import com.mobicoolsoft.electronic.store.dto.PageResponse;
import com.mobicoolsoft.electronic.store.exception.FileNotAvailableException;
import com.mobicoolsoft.electronic.store.service.CategoryServiceI;
import com.mobicoolsoft.electronic.store.service.FileServiceI;
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

@RestController
@RequestMapping(AppConstants.CATEGORY_URL)
public class CategoryController {

    private final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryServiceI categoryServiceI;

    @Autowired
    private FileServiceI fileServiceI;

    @Value("${category.profile.image.path}")
    private String imagePath;

    @PostMapping("/")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto category = this.categoryServiceI.createCategory(categoryDto);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable String categoryId) {
        CategoryDto updateCategory = this.categoryServiceI.updateCategory(categoryDto, categoryId);
        return new ResponseEntity<>(updateCategory, HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId) {
        this.categoryServiceI.deleteCategory(categoryId);
        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
                .message("Category" + AppConstants.DELETE_MSG)
                .success(true)
                .status(HttpStatus.OK).build();
        return new ResponseEntity<>(apiResponseMessage, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<PageResponse<CategoryDto>> getAllCategories(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORY_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir)
    {
        PageResponse<CategoryDto> pageResponse = this.categoryServiceI.getAllCategories(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable String categoryId) {
        CategoryDto categoryDto = this.categoryServiceI.getCategoryById(categoryId);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<CategoryDto>> searchCategoryByKyeword(@PathVariable String keyword) {
        List<CategoryDto> categoryDtos = this.categoryServiceI.searchCategoryByTitleKeyword(keyword);
        return new ResponseEntity<>(categoryDtos, HttpStatus.OK);
    }
    @PostMapping("/upload/images/{categoryId}")
    public ResponseEntity<ImageResponse> uploadCategoryImage(
            @RequestParam("categoryImage")MultipartFile categoryImage,
            @PathVariable String categoryId) throws IOException
    {
        CategoryDto categoryDto = this.categoryServiceI.getCategoryById(categoryId);
        String uploadCategoryImage = this.fileServiceI.uploadImage(imagePath, categoryImage);
        categoryDto.setCoverImage(uploadCategoryImage);
        this.categoryServiceI.updateCategory(categoryDto, categoryId);
        ImageResponse imageResponse = ImageResponse.builder()
                .imageName(uploadCategoryImage).message("Category" + AppConstants.IMAGE_MSG)
                .success(true).status(HttpStatus.CREATED).build();
        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
    }

    @GetMapping("/images/{categoryId}")
    public void serveCategoryImage(@PathVariable String categoryId, HttpServletResponse response) throws FileNotAvailableException {
        CategoryDto categoryDto = this.categoryServiceI.getCategoryById(categoryId);
        try {
            InputStream serveImage = this.fileServiceI.serveImage(imagePath, categoryDto.getCoverImage());
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            StreamUtils.copy(serveImage, response.getOutputStream());
        }catch (IOException ex){
            throw new FileNotAvailableException(categoryId);
        }
    }
}
