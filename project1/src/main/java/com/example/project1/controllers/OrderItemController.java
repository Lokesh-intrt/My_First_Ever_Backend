package com.example.project1.controllers;

import com.example.project1.DTOs.OrderItemRequestDTO;
import com.example.project1.DTOs.OrderItemResponseDTO;
import com.example.project1.service.OrderItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orderItems")
public class OrderItemController {

    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @DeleteMapping("/remove/{itemId}")
    public ResponseEntity<Void> removeOrderItem(@PathVariable Long itemId)
    {
        orderItemService.removeOrderItem(itemId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/modify")
    public ResponseEntity<OrderItemResponseDTO> modifyOrderItem(@RequestBody OrderItemRequestDTO orderItemRequestDTO)
    {
        return ResponseEntity.ok(orderItemService.modifyOrderItem(orderItemRequestDTO));
    }

}
