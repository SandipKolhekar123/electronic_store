package com.mobicoolsoft.electronic.store.service;

import com.mobicoolsoft.electronic.store.dto.CategoryDto;
import com.mobicoolsoft.electronic.store.dto.PageResponse;

import java.util.List;

public interface CategoryServiceI {

    /**
     * @implNote create category
     */
    CategoryDto createCategory(CategoryDto categoryDto);

    /**
     * @implNote update category
     */
    CategoryDto updateCategory(CategoryDto categoryDto, String categoryId);

    /**
     * @implNote delete category
     */
    void deleteCategory(String categoryId);

    /**
     * @implNote get All category
     */
    PageResponse<CategoryDto> getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    /**
     * @implNote get single category by id
     */
    CategoryDto getCategoryById(String categoryId);

    /**
     * @implNote search category by keyword
     */
    PageResponse<CategoryDto> searchCategoryByTitleKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
}
