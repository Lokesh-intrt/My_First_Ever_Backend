package com.example.project1.mappers;

import com.example.project1.DTOs.CartItemResponseDTO;
import com.example.project1.model.CartItem;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MapCartItemResponseDTO {

    @Mapping(source = "product.name", target = "itemName")
    @Mapping(source = "product.price", target = "price")
    @Mapping(source = "product.seller.name", target = "seller")
    @Mapping(source = "product.status", target = "productStatus")
    @Mapping(source = "product.stock", target = "stock")
    CartItemResponseDTO toDTO(CartItem cartItem);
}
