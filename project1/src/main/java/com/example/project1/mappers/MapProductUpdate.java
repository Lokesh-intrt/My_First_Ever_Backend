package com.example.project1.mappers;

import com.example.project1.DTOs.ProductUpdateDTO;
import com.example.project1.model.Product;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface MapProductUpdate {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toUpdateProduct(ProductUpdateDTO productUpdateDTO, @MappingTarget Product product);
}
