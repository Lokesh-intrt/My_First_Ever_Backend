package com.example.project1.DTOs;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemModifyDTO {

    private Long cartItemId;

    private Integer newQuantity;
}
