package com.example.project1.mappers;

import com.example.project1.model.OrderItem;
import com.example.project1.model.Product;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface MapProductOrderItem {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toOrderItem(Product product, @MappingTarget OrderItem orderItem);
}
