package com.example.project1.DTOs;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PayResponseDTO {

    @Min(0)
    private Integer amount;

    @NotNull
    private String orderId;

    private String currencyType;

    private String keyId;
}
