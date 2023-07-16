package com.mobicoolsoft.electronic.store.service.impl;

import com.mobicoolsoft.electronic.store.config.AppConstants;
import com.mobicoolsoft.electronic.store.dto.PageResponse;
import com.mobicoolsoft.electronic.store.dto.ProductDto;
import com.mobicoolsoft.electronic.store.entity.Product;
import com.mobicoolsoft.electronic.store.exception.IllegalArgumentsException;
import com.mobicoolsoft.electronic.store.exception.ResourceNotFoundException;
import com.mobicoolsoft.electronic.store.helper.PageHelper;
import com.mobicoolsoft.electronic.store.repository.ProductRepository;
import com.mobicoolsoft.electronic.store.service.ProductServiceI;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductServiceI {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        String productId = UUID.randomUUID().toString();
        Product product = this.modelMapper.map(productDto, Product.class);
        product.setId(productId);
        product.setAddedDate(new Date());
        Product savedProduct = this.productRepository.save(product);
        return this.modelMapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, String productId) {
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setImage(productDto.getImage());
        product.setPrice(productDto.getPrice());
        product.setDiscount(productDto.getDiscount());
        product.setQuantity(productDto.getQuantity());
        product.setLive(productDto.getLive());
        product.setStock(productDto.getStock());
        product.setUpdatedDate(new Date());
        Product savedProduct = this.productRepository.save(product);
        return this.modelMapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public void deleteProduct(String productId) {
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        this.productRepository.delete(product);
    }

    @Override
    public PageResponse<ProductDto> getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        try {
            Sort sort = (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());
            Pageable pageable = PageRequest.of(pageNumber-1, pageSize, sort);
            Page<Product> productPage = this.productRepository.findAll(pageable);
            PageResponse<ProductDto> pageResponse = PageHelper.getPageResponse(productPage, ProductDto.class);
            return pageResponse;
        }catch (RuntimeException ex){
            throw new IllegalArgumentsException(AppConstants.PAGE_ERROR_MSG);
        }
    }

    @Override
    public ProductDto getProductById(String productId) {
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        return this.modelMapper.map(product, ProductDto.class);
    }

    @Override
    public PageResponse<ProductDto> getByTitleContaining(String subTitle, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            Pageable pageable = PageRequest.of(pageNumber-1, pageSize, sort);
            Page<Product> productPage = this.productRepository.findByTitleContaining(subTitle, pageable);
            PageResponse<ProductDto> pageResponse = PageHelper.getPageResponse(productPage, ProductDto.class);
            return pageResponse;
        }catch (RuntimeException ex){
            throw new IllegalArgumentsException(AppConstants.PAGE_ERROR_MSG);
        }
    }

    @Override
    public PageResponse<ProductDto> getByLiveTrue(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            Pageable pageable = PageRequest.of(pageNumber-1, pageSize, sort);
            Page<Product> productPage = this.productRepository.findByLiveTrue(pageable);
            PageResponse<ProductDto> pageResponse = PageHelper.getPageResponse(productPage, ProductDto.class);
            return pageResponse;
        }catch (RuntimeException ex){
            throw new IllegalArgumentsException(AppConstants.PAGE_ERROR_MSG);
        }
    }
}
