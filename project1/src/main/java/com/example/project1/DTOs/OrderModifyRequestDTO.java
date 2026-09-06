package com.example.project1.DTOs;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderModifyRequestDTO {

    @NotNull
    private Long orderId;

    private Integer quantity;
}
