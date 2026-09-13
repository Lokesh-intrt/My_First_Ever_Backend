package com.example.project1.DTOs;

import com.example.project1.model.Product;
import com.example.project1.model.User;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponseDTO {

    @NotNull
    @NotBlank
    private String itemName;

    @NotNull
    @DecimalMin("0")
    private Double price;

    @NotNull
    @Min(1)
    private Integer quantity;

    @NotNull
    private String seller;

    @NotNull
    private Product.ProductStatus productStatus;

    @Min(1)
    private Integer stock;
}
