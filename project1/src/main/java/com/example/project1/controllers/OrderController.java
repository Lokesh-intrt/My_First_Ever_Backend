package com.example.project1.controllers;

import com.example.project1.DTOs.OrderRequestDTO;
import com.example.project1.DTOs.OrderResponseDTO;
import com.example.project1.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/place")
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO, Principal principal)
    {
        return ResponseEntity.ok(orderService.createOrder(orderRequestDTO,principal));
    }

    @DeleteMapping("/cancel/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId)
    {
        orderService.cancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}