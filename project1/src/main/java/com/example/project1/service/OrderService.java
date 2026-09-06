package com.example.project1.service;

import com.example.project1.DTOs.OrderItemRequestDTO;
import com.example.project1.DTOs.OrderRequestDTO;
import com.example.project1.DTOs.OrderResponseDTO;
import com.example.project1.mappers.MapOrderOrderResponse;
import com.example.project1.model.Order;
import com.example.project1.model.OrderItem;
import com.example.project1.model.User;
import com.example.project1.repositories.OrderRepository;
import com.example.project1.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@PreAuthorize("isAuthenticated()")
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;
    private final UserRepository userRepository;
    private final MapOrderOrderResponse mapOrderOrderResponse;

    public OrderService(OrderRepository orderRepository, OrderItemService orderItemService, UserRepository userRepository, MapOrderOrderResponse mapOrderOrderResponse) {
        this.orderRepository = orderRepository;
        this.orderItemService = orderItemService;
        this.userRepository = userRepository;
        this.mapOrderOrderResponse = mapOrderOrderResponse;
    }

    @Transactional(rollbackOn = Error.class)
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO, Principal principal)
    {
        User user = userRepository.findByEmail(principal.getName()).orElseThrow(()->new EntityNotFoundException("User"));

        List<OrderItem> orderItems = new ArrayList<>();
        int totalQuantity = 0;
        double totalCost = 0.0;

        for (OrderItemRequestDTO itemRequest : orderRequestDTO.getItems()) {
            OrderItem orderItem = orderItemService.createOrderItem(itemRequest);
            orderItems.add(orderItem);
            totalQuantity += orderItem.getQuantity();
            totalCost += calculateTotalCost(orderItem.getQuantity(), orderItem.getPrice());
        }

        Order order = Order.builder().quantity(totalQuantity)
                .orderStatus(Order.OrderStatus.EN_ROUTE)
                .user(user)
                .totalAmount(totalCost)
                .orderItems(orderItems)
                .build();

        orderItems.forEach(orderItem -> orderItem.setOrder(order));
        orderRepository.save(order);
        OrderResponseDTO orderResponse = new OrderResponseDTO();
        return mapOrderOrderResponse.toResponse(order,orderResponse);
    }

    public static Double calculateTotalCost(Integer quantity, Double cost)
    {
        return cost*quantity;
    }

    @Transactional
    public void cancelOrder(Long orderId)
    {
        Order order = orderRepository.findById(orderId).orElseThrow(()->new EntityNotFoundException("order"));
        orderRepository.delete(order);
    }

    @Transactional
    @Lock(LockModeType.OPTIMISTIC)
    public void modifyOrder(Long orderId)
    {
        Order order = orderRepository.findById(orderId).orElseThrow(()->new EntityNotFoundException("order"));

        int totalQuantity = 0 ;
        double totalCost = 0.0;
        List<OrderItem> orderItems = order.getOrderItems();
        for(OrderItem orderItem: orderItems)
        {
            totalQuantity += orderItem.getQuantity();
            totalCost += calculateTotalCost(orderItem.getQuantity(),orderItem.getPrice());
        }

        order.setQuantity(totalQuantity);
        order.setTotalAmount(totalCost);
    }
}
