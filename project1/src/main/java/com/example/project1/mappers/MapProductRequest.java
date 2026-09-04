package com.example.project1.mappers;

import com.example.project1.DTOs.ProductRequestDTO;
import com.example.project1.model.Product;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface MapProductRequest {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toProduct(ProductRequestDTO productRequestDTO, @MappingTarget Product product);
}
