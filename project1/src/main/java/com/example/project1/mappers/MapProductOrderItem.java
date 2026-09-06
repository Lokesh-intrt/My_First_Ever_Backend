package com.example.project1.mappers;

import com.example.project1.model.OrderItem;
import com.example.project1.model.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MapProductOrderItem {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "name", target = "itemName")
    void toOrderItem(Product product, @MappingTarget OrderItem orderItem);
}
