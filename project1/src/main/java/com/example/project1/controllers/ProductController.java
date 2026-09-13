package com.example.project1.controllers;

import com.example.project1.DTOs.ProductRequestDTO;
import com.example.project1.DTOs.ProductUpdateDTO;
import com.example.project1.model.Product;
import com.example.project1.service.ProductService;
import com.example.project1.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    public ResponseEntity<Product> createProduct
            (Principal principal,@Valid @RequestBody ProductRequestDTO productRequestDTO)
    {
        return ResponseEntity.status(HttpStatus.CREATED).
                body(productService.createProduct(principal.getName(), productRequestDTO));
    }

    @GetMapping("/get/bystatus")
    public ResponseEntity<Page<Product>> viewProductsByStatus(@RequestParam  Product.ProductStatus status,
                                                              @RequestParam @Min(0) int page,
                                                              @RequestParam @Min(0) int size,
                                                              @RequestParam String sortBy,
                                                              @RequestParam Sort.Direction direction)
    {
        return ResponseEntity.ok(productService.viewProductsByStatus(status,page,size,sortBy,direction));
    }

    @GetMapping("/get/all")
    ResponseEntity<Page<Product>>  viewAllProducts(@RequestParam @Min(0) int page,
                                                   @RequestParam @Min(0) int size,
                                                   @RequestParam String sortBy,
                                                   @RequestParam Sort.Direction direction)
    {
        return ResponseEntity.ok(productService.viewAllProducts(page, size, sortBy, direction));
    }

    @GetMapping("/get/bySeller")
    public ResponseEntity<Page<Product>> viewProductsBySeller(Principal principal,
                                                              @RequestParam @Min(0) int page,
                                                              @RequestParam @Min(0) int size,
                                                              @RequestParam String sortBy,
                                                              @RequestParam Sort.Direction direction)
    {
        return ResponseEntity.ok(productService.viewProductsBySeller(principal.getName(),page,size,sortBy,direction));
    }


    @PatchMapping("/patch/{id}")
    public ResponseEntity<Product> updateProduct(Principal principal,@PathVariable Long id, @Valid @RequestBody ProductUpdateDTO productUpdateDTO)
    {
        return ResponseEntity.status(HttpStatus.OK).body(productService.updateProduct(principal.getName(),id,productUpdateDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(Principal principal,@PathVariable Long id)
    {
        productService.deleteProduct(principal.getName(),id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
