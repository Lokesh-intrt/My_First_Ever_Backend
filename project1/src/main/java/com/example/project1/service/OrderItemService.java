package com.example.project1.service;

import com.example.project1.DTOs.OrderItemRequestDTO;
import com.example.project1.mappers.MapProductOrderItem;
import com.example.project1.model.OrderItem;
import com.example.project1.model.Product;
import com.example.project1.repositories.OrderItemRepository;
import com.example.project1.repositories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@PreAuthorize("isAuthenticated()")
@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final MapProductOrderItem mapProductOrderItem;

    public OrderItemService(OrderItemRepository orderItemRepository, ProductRepository productRepository, MapProductOrderItem mapProductOrderItem) {
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.mapProductOrderItem = mapProductOrderItem;
    }

    @Transactional
    public OrderItem createOrderItem(OrderItemRequestDTO orderItemRequestDTO)
    {
        Product product = productRepository.findById(orderItemRequestDTO.getId()).orElseThrow(()->new EntityNotFoundException("product"));
        OrderItem orderItem = new OrderItem();
        mapProductOrderItem.toOrderItem(product,orderItem);
        orderItemRepository.save(orderItem);
        return orderItem;
    }
}
