package com.mobicoolsoft.electronic.store.service;

import com.mobicoolsoft.electronic.store.dto.CategoryDto;
import com.mobicoolsoft.electronic.store.dto.PageResponse;
import com.mobicoolsoft.electronic.store.dto.ProductDto;

public interface ProductServiceI {

    /**
     * @implNote create product
     */
    ProductDto createProduct(ProductDto productDto);


    /**
     * @implNote update product
     */
    ProductDto updateProduct(ProductDto productDto, String productId);


    /**
     * @implNote delete product
     */
    void deleteProduct(String productId);

    /**
     * @implNote get All products
     */
    PageResponse<ProductDto> getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);


    /**
     * @implNote get product by product_id
     */
    ProductDto getProductById(String productId);


    /**
     * @implNote search products by keyword
     */
    PageResponse<ProductDto> getByTitleContaining(String subTitle, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);


    /**
     * @implNote search products by availability i.e Live
     */
    PageResponse<ProductDto> getByLiveTrue(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
}
