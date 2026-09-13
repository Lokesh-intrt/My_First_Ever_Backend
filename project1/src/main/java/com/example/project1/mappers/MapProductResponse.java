package com.example.project1.mappers;

import com.example.project1.DTOs.ProductResponseDTO;
import com.example.project1.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MapProductResponse {

    @Mapping(source = "seller.name", target = "seller")
    public ProductResponseDTO toDTO(Product product);
}
