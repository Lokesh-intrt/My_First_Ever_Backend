package com.example.project1.controllers;

import com.example.project1.DTOs.ProductRequestDTO;
import com.example.project1.DTOs.ProductUpdateDTO;
import com.example.project1.model.Product;
import com.example.project1.service.ProductService;
import com.example.project1.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final UserService userService;
    private final ProductService productService;

    public ProductController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    @PostMapping("/create")
    public ResponseEntity<Product> createProduct
            (Principal principal,@Valid @RequestBody ProductRequestDTO productRequestDTO)
    {
        return ResponseEntity.status(HttpStatus.CREATED).
                body(productService.createProduct(principal.getName(), productRequestDTO));
    }

    @GetMapping("/get/all")
    ResponseEntity<Page<Product>>  viewAllProducts(@RequestParam int page,
                                                   @RequestParam int size,
                                                   @RequestParam String sortBy,
                                                   @RequestParam String direction)
    {
        return ResponseEntity.ok(productService.viewAllProducts(page, size, sortBy, direction));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id)
    {
        return ResponseEntity.status(HttpStatus.OK).body(productService.viewProduct(id));
    }

    @PatchMapping("/patch/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductUpdateDTO productUpdateDTO)
    {
        return ResponseEntity.status(HttpStatus.OK).body(productService.updateProduct(id,productUpdateDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id)
    {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
