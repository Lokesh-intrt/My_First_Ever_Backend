package com.example.project1.DTOs;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartRequestDTO {

    @NotNull
    @Valid
    private List<CartItemRequestDTO> cartItemRequestDTOS;
}
