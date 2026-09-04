package com.example.project1.DTOs;

import jakarta.validation.constraints.DecimalMin;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateDTO {

    private String name;

    @DecimalMin("0.00")
    private Double price;

    private Integer stock;
}
