package com.example.project1.repositories;

import com.example.project1.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {

    Optional<Product> findByProductIdAndSeller_Email(Long productId,String email);

    Page<Product> findBySeller_Email(String email, Pageable pageable);

    Page<Product> findByStatus(Product.ProductStatus status,Pageable pageable);
}
