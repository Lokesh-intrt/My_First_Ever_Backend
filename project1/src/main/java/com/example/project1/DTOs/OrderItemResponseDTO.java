package com.example.project1.DTOs;

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
public class OrderItemResponseDTO {

    @NotBlank
    private String itemName;

    @NotNull
    @DecimalMin(value = "0")
    private Double price;

    @NotNull
    @Min(value = 0)
    private Integer quantity;
}
