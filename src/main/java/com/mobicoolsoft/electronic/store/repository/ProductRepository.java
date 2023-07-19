package com.mobicoolsoft.electronic.store.repository;

import com.mobicoolsoft.electronic.store.entity.Category;
import com.mobicoolsoft.electronic.store.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

   Page<Product> findByTitleContaining(String subtitle, Pageable pageable);

   Page<Product> findByLiveTrue(Pageable pageable);

   Page<Product> findByCategory(Category category, Pageable pageable);
}
