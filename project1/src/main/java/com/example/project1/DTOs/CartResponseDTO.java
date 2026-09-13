package com.example.project1.DTOs;

import lombok.*;

import java.util.List;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseDTO {

    private List<CartItemResponseDTO> cartItems;

    private Integer totalQuantity;

    private Double totalCheckoutPrice;
}
