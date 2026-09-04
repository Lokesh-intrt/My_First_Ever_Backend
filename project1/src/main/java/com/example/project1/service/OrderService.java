package com.example.project1.service;

import com.example.project1.DTOs.OrderItemRequestDTO;
import com.example.project1.model.Order;
import com.example.project1.model.OrderItem;
import com.example.project1.model.User;
import com.example.project1.repositories.OrderRepository;
import com.example.project1.repositories.ProductRepository;
import com.example.project1.repositories.UserRepository;
import jakarta.persistence.CascadeType;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@PreAuthorize("isAuthenticated()")
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderItemService orderItemService;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository , OrderItemService orderItemService, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderItemService = orderItemService;
        this.userRepository = userRepository;
    }

    @Transactional(rollbackOn = Error.class)
    public Order createOrder(OrderItemRequestDTO orderItemRequestDTO, Principal principal)
    {
        OrderItem orderItem = orderItemService.createOrderItem(orderItemRequestDTO);
        Double totalCost = calculateTotalCost(orderItemRequestDTO.getQuantity(),orderItem.getPrice());

        User user = userRepository.findByEmail(principal.getName()).orElseThrow(()->new EntityNotFoundException("User"));

        Order order = Order.builder().quantity(orderItemRequestDTO.getQuantity())
                .orderStatus(Order.OrderStatus.EN_ROUTE).user(user).totalAmount(totalCost).build();

        orderRepository.save(order);
        return order;
    }

    public static Double calculateTotalCost(Integer quantity, Double cost)
    {
        return cost*quantity;
    }


}
