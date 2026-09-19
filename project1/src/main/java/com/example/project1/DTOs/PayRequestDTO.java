package com.example.project1.DTOs;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PayRequestDTO {

    @DecimalMin("0")
    private Double amount;

    @NotNull
    private Long orderId;

    private String currencyType;
}

