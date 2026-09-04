package com.example.project1.DTOs;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @DecimalMin("0.00")
    private Double price;

    @NotNull
    @Min(1)
    private Integer stock;
}
