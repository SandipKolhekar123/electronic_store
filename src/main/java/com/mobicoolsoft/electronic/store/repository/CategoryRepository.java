package com.mobicoolsoft.electronic.store.repository;

import com.mobicoolsoft.electronic.store.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, String> {

    Page<Category> findByTitleContaining(String keyword, Pageable pageable);
}
