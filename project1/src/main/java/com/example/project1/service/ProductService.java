package com.example.project1.service;

import com.example.project1.DTOs.ProductRequestDTO;
import com.example.project1.DTOs.ProductUpdateDTO;
import com.example.project1.mappers.MapProductRequest;
import com.example.project1.mappers.MapProductUpdate;
import com.example.project1.model.Product;
import com.example.project1.model.User;
import com.example.project1.repositories.ProductRepository;
import com.example.project1.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.FetchType;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@PreAuthorize("hasRole('SELLER')")
public class ProductService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final MapProductRequest mapProductRequest;
    private final MapProductUpdate mapProductUpdate;

    public ProductService(UserRepository userRepository, ProductRepository productRepository, MapProductRequest mapProductRequest, MapProductUpdate mapProductUpdate) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.mapProductRequest = mapProductRequest;
        this.mapProductUpdate = mapProductUpdate;
    }

    @Transactional
    public Product createProduct(String email, ProductRequestDTO requestDTO)
    {
        User user = userRepository.findByEmail(email).orElseThrow(()->new EntityNotFoundException("user"));

        Product product = new Product();
        mapProductRequest.toProduct(requestDTO,product);

        product.setSeller(user);
        product.setStatus(Product.ProductStatus.AVAILABLE);

        return productRepository.save(product);
    }

    @PreAuthorize("isAuthenticated()")
    public Page<Product> viewAllProducts(int page, int size, String sortBy, String direction)
    {
        Sort sort =direction.equalsIgnoreCase(Sort.Direction.ASC.name())
                    ?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page,size,sort);
        return productRepository.findAll(pageable);
    }

    @Transactional
    public Product updateProduct(Long id, ProductUpdateDTO requestDTO)
    {
        Product product = productRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("product"));

        mapProductUpdate.toUpdateProduct(requestDTO,product);

        return product;
    }

    @Transactional
    public void deleteProduct(Long id)
    {
        Product product = productRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("product"));

        productRepository.delete(product);
    }
}
