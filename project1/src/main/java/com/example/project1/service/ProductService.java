package com.example.project1.service;

import com.example.project1.DTOs.ProductRequestDTO;
import com.example.project1.DTOs.ProductUpdateDTO;
import com.example.project1.exceptions.IllegalQuantityException;
import com.example.project1.mappers.MapProductRequest;
import com.example.project1.mappers.MapProductUpdate;
import com.example.project1.model.Product;
import com.example.project1.model.User;
import com.example.project1.repositories.ProductRepository;
import com.example.project1.repositories.UserRepository;
import com.example.project1.exceptions.ResourceNotFoundException;
import jakarta.persistence.FetchType;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ProductService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final MapProductRequest mapProductRequest;
    private final MapProductUpdate mapProductUpdate;
    private static final Set<String> ALLOWED_SORTING_FIELDS = Set.of("name","price","status","stock");


    public ProductService(UserRepository userRepository, ProductRepository productRepository, MapProductRequest mapProductRequest, MapProductUpdate mapProductUpdate) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.mapProductRequest = mapProductRequest;
        this.mapProductUpdate = mapProductUpdate;
    }

    private static Boolean isAllowedSortByField(String sortBy)
    {
        return ALLOWED_SORTING_FIELDS.contains(sortBy);
    }

    @PreAuthorize("hasRole('SELLER')")
    @Transactional
    public Product createProduct(String email, ProductRequestDTO requestDTO)
    {
        User user = userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("user"));

        Product product = new Product();
        mapProductRequest.toProduct(requestDTO,product);

        product.setSeller(user);
        product.setStatus(Product.ProductStatus.AVAILABLE);

        return productRepository.save(product);
    }

    @PreAuthorize("isAuthenticated()")
    public Page<Product> viewProductsByStatus(Product.ProductStatus status, int page, int size, String sortBy, Sort.Direction direction)
    {
        if(!isAllowedSortByField(sortBy))
            throw new IllegalArgumentException("this sorting is not available!");
        Sort sort = Sort.by(direction,sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return productRepository.findByStatus(status, pageable);
    }

    @PreAuthorize("isAuthenticated()")
    public Page<Product> viewProductsBySeller(String email,int page, int size, String sortBy, Sort.Direction direction)
    {
        if(!isAllowedSortByField(sortBy))
            throw new IllegalArgumentException("this sorting is not available!");
        Sort sort = Sort.by(direction,sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return productRepository.findBySeller_Email(email, pageable);
    }


    @PreAuthorize("isAuthenticated()")
    public Page<Product> viewAllProducts(int page, int size, String sortBy, Sort.Direction direction)
    {
        if(!isAllowedSortByField(sortBy))
            throw new IllegalArgumentException("this sorting is not available!");
        Sort sort = Sort.by(direction,sortBy);
        Pageable pageable = PageRequest.of(page,size,sort);
        return productRepository.findAll(pageable);
    }

    @PreAuthorize("isAuthenticated()")
    @Cacheable(value = "productById", key = "#id")
    public Product viewProductById(Long id)
    {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("product", id));
    }

    @Transactional
    @PreAuthorize("hasRole('SELLER')")
    @Caching(
            put = @CachePut(value = "product", key = "#email"),
            evict = @CacheEvict(value = "productById", key = "#id")
    )
    public Product updateProduct(String email,Long id, ProductUpdateDTO requestDTO)
    {
        Product product = productRepository.findByProductIdAndSeller_Email(id,email).orElseThrow(()-> new ResourceNotFoundException("product"));

        mapProductUpdate.toUpdateProduct(requestDTO,product);

        return product;
    }

    @Transactional
    @PreAuthorize("hasRole('SELLER')")
    @Caching(evict = {
            @CacheEvict(value = "product", key = "#email"),
            @CacheEvict(value = "productById", key = "#id")
    })
    public void deleteProduct(String email,Long id)
    {
        Product product = productRepository.findByProductIdAndSeller_Email(id,email).orElseThrow(()-> new ResourceNotFoundException("product"));

        productRepository.delete(product);
    }
}
