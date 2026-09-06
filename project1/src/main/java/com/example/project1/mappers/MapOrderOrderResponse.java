package com.example.project1.mappers;

import com.example.project1.DTOs.OrderResponseDTO;
import com.example.project1.model.Order;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MapOrderOrderResponse {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source= "user.name", target="userName")
    @Mapping(source="order.orderItems", target="orderItemResponse")
    OrderResponseDTO toResponse(Order order ,@MappingTarget OrderResponseDTO orderResponseDTO);
}
