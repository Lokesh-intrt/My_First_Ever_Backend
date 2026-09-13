package com.example.project1.service;

import com.example.project1.DTOs.OrderItemRequestDTO;
import com.example.project1.DTOs.OrderItemResponseDTO;
import com.example.project1.exceptions.IllegalQuantityException;
import com.example.project1.exceptions.ResourceNotFoundException;
import com.example.project1.mappers.MapProductOrderItem;
import com.example.project1.model.Order;
import com.example.project1.model.OrderItem;
import com.example.project1.model.Product;
import com.example.project1.repositories.OrderItemRepository;
import com.example.project1.repositories.OrderRepository;
import com.example.project1.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

@PreAuthorize("isAuthenticated()")
@Service
public class OrderItemService {

    private final ProductRepository productRepository;
    private final MapProductOrderItem mapProductOrderItem;
    private final OrderItemRepository orderItemRepository;
    private final OrderService orderService;
    private final OrderRepository orderRepository;

    public OrderItemService(ProductRepository productRepository, MapProductOrderItem mapProductOrderItem,
                            OrderItemRepository orderItemRepository, @Lazy OrderService orderService, @Lazy OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.mapProductOrderItem = mapProductOrderItem;
        this.orderItemRepository = orderItemRepository;
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderItem createOrderItem(OrderItemRequestDTO orderItemRequestDTO)
    {

        Product product = productRepository.findById(orderItemRequestDTO.getId()).orElseThrow(()->new ResourceNotFoundException("product"));
        if(orderItemRequestDTO.getQuantity()>product.getStock())
            throw new IllegalQuantityException(product.getName());
        OrderItem orderItem = new OrderItem();
        mapProductOrderItem.toOrderItem(product,orderItem);
        orderItem.setProduct(product);
        orderItem.setQuantity(orderItemRequestDTO.getQuantity());
        return orderItem;
    }

    @Transactional
    public void removeOrderItem(Long itemId)
    {
        OrderItem orderItem = orderItemRepository.findById(itemId).orElseThrow(()->new ResourceNotFoundException("orderItem"));
        Order order = orderRepository.findById(orderItem.getOrder().getOrderId()).orElseThrow(()->new ResourceNotFoundException("order"));
        order.getOrderItems().remove(orderItem);
        orderService.modifyOrder(order.getOrderId());
        orderItem.setOrder(null);

        orderItemRepository.delete(orderItem);
    }

    @Transactional
    public OrderItemResponseDTO modifyOrderItem(OrderItemRequestDTO orderItemRequestDTO)
    {
        OrderItem orderItem = orderItemRepository.findById(orderItemRequestDTO.getId()).orElseThrow(()->new ResourceNotFoundException("orderItem"));

        Product product = productRepository.findById(orderItem.getProduct().getProductId()).orElseThrow(()->new ResourceNotFoundException("product"));
        if(product.getStock()<orderItemRequestDTO.getQuantity())
            throw new IllegalQuantityException(product.getName());
        orderItem.setQuantity(orderItemRequestDTO.getQuantity());
        orderService.modifyOrder(orderItem.getOrder().getOrderId());
        return new OrderItemResponseDTO(orderItem.getItemName(), orderItem.getPrice(), orderItem.getQuantity());
    }
}
