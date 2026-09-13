package com.example.project1.mappers;

import com.example.project1.DTOs.CartResponseDTO;
import com.example.project1.model.Cart;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = MapCartItemResponseDTO.class)
public interface MapCartCartResponse {

    @Mapping(source = "totalAmount", target = "totalCheckoutPrice")
    CartResponseDTO toDTO(Cart cart);
}
