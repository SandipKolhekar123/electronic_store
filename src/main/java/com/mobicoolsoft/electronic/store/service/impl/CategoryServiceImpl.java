package com.mobicoolsoft.electronic.store.service.impl;
import com.mobicoolsoft.electronic.store.config.AppConstants;
import com.mobicoolsoft.electronic.store.dto.CategoryDto;
import com.mobicoolsoft.electronic.store.dto.PageResponse;
import com.mobicoolsoft.electronic.store.entity.Category;
import com.mobicoolsoft.electronic.store.exception.IllegalArgumentsException;
import com.mobicoolsoft.electronic.store.exception.ResourceNotFoundException;
import com.mobicoolsoft.electronic.store.helper.PageHelper;
import com.mobicoolsoft.electronic.store.repository.CategoryRepository;
import com.mobicoolsoft.electronic.store.service.CategoryServiceI;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryServiceI {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        String randomId = UUID.randomUUID().toString();
        Category category = this.modelMapper.map(categoryDto, Category.class);
        category.setId(randomId);
        category.setCreatedBy(categoryDto.getCreatedBy());
        Category savedCategory = this.categoryRepository.save(category);
        return this.modelMapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, String categoryId) {
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());
        category.setUpdatedBy(categoryDto.getUpdatedBy());
        Category savedCategory = this.categoryRepository.save(category);
        return this.modelMapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    public void deleteCategory(String categoryId) {
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        this.categoryRepository.delete(category);
    }

    @Override
    public PageResponse<CategoryDto> getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        try{
            Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            Pageable pageable = PageRequest.of(pageNumber-1, pageSize, sort);
            Page<Category> categoryPage = this.categoryRepository.findAll(pageable);
            PageResponse<CategoryDto> pageResponse = PageHelper.getPageResponse(categoryPage, CategoryDto.class);
            return pageResponse;
        }catch (RuntimeException ex){
            throw new IllegalArgumentsException(AppConstants.PAGE_ERROR_MSG);
        }
    }

    @Override
    public CategoryDto getCategoryById(String categoryId) {
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        return this.modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public PageResponse<CategoryDto> searchCategoryByTitleKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        try{
            Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            Pageable pageable = PageRequest.of(pageNumber-1, pageSize, sort);
            Page<Category> categoryPage = this.categoryRepository.findByTitleContaining(keyword ,pageable);
            PageResponse<CategoryDto> pageResponse = PageHelper.getPageResponse(categoryPage, CategoryDto.class);
            return pageResponse;
        }catch (RuntimeException ex){
            throw new IllegalArgumentsException(AppConstants.PAGE_ERROR_MSG);
        }
    }
}
