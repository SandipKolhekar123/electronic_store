package com.mobicoolsoft.electronic.store.controller;

import com.mobicoolsoft.electronic.store.config.AppConstants;
import com.mobicoolsoft.electronic.store.dto.ApiResponseMessage;
import com.mobicoolsoft.electronic.store.dto.ImageResponse;
import com.mobicoolsoft.electronic.store.dto.PageResponse;
import com.mobicoolsoft.electronic.store.dto.ProductDto;
import com.mobicoolsoft.electronic.store.exception.FileNotAvailableException;
import com.mobicoolsoft.electronic.store.service.FileServiceI;
import com.mobicoolsoft.electronic.store.service.ProductServiceI;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping(AppConstants.PRODUCT_URL)
public class ProductController {

    @Autowired
    private ProductServiceI productServiceI;

    @Autowired
    private FileServiceI fileServiceI;

    @Value("${product.profile.image.path}")
    private String imagePath;

    @PostMapping("/")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        ProductDto product = this.productServiceI.createProduct(productDto);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto, @PathVariable String productId) {
        ProductDto updatedProduct = this.productServiceI.updateProduct(productDto, productId);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String productId) {
        this.productServiceI.deleteProduct(productId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder()
                .message(AppConstants.DELETE_MSG).success(true)
                .status(HttpStatus.OK).build();
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<PageResponse<ProductDto>> getAllProducts(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_PRODUCT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {
        PageResponse<ProductDto> pageResponse = this.productServiceI.getAllProducts(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable String productId) {
        ProductDto productDto = this.productServiceI.getProductById(productId);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @GetMapping("/search/{query}")
    public ResponseEntity<PageResponse<ProductDto>> searchProduct(@PathVariable String query,
                                                                  @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                  @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                  @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_PRODUCT_BY, required = false) String sortBy,
                                                                  @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {
        PageResponse<ProductDto> pageResponse = this.productServiceI.getByTitleContaining(query, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @GetMapping("/live")
    public ResponseEntity<PageResponse<ProductDto>> getAllLive(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_PRODUCT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {
        PageResponse<ProductDto> pageResponse = this.productServiceI.getByLiveTrue(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/upload/images/{productId}")
    public ResponseEntity<ImageResponse> uploadProductImage(
            @RequestParam("productImage") MultipartFile productImage,
            @PathVariable String productId) throws IOException {
        ProductDto productDto = this.productServiceI.getProductById(productId);
        String uploadProductImage = this.fileServiceI.uploadImage(imagePath, productImage);
        productDto.setImage(uploadProductImage);
        this.productServiceI.updateProduct(productDto, productId);
        ImageResponse imageResponse = ImageResponse.builder()
                .imageName(uploadProductImage).message("Product" + AppConstants.IMAGE_MSG)
                .success(true).status(HttpStatus.CREATED).build();
        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
    }

    @GetMapping("/images/{productId}")
    public void serveProductImage(HttpServletResponse response, @PathVariable String productId) throws FileNotAvailableException {
        ProductDto productDto = this.productServiceI.getProductById(productId);
        try {
            InputStream serveImage = this.fileServiceI.serveImage(imagePath, productDto.getImage());
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            StreamUtils.copy(serveImage, response.getOutputStream());
        } catch (IOException ex) {
            throw new FileNotAvailableException(productId);
        }
    }
}
