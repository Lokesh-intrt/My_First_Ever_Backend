package com.example.project1.DTOs;

import com.example.project1.model.Product;
import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {

    private String name;

    private Double price;

    private String seller;

    private Integer stock;

    private Product.ProductStatus status;
}
