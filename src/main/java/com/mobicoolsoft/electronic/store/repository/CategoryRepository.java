package com.mobicoolsoft.electronic.store.repository;

import com.mobicoolsoft.electronic.store.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, String> {

    List<Category> findByTitleContaining(String keyword);
}
