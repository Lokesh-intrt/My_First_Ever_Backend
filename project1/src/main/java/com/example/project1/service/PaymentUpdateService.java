package com.example.project1.service;

import com.example.project1.model.Order;
import com.example.project1.repositories.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentUpdateService {

    private final OrderRepository orderRepository;

    public PaymentUpdateService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Async
    @Transactional
    public void updateOrderStatus(Long orderId)
    {
        Order order = orderRepository.findById(orderId).orElseThrow(()->new EntityNotFoundException("order"));

        order.setOrderStatus(Order.OrderStatus.PAID);
        orderRepository.save(order);

        log.info("order status was updated after payment success");
    }
}
