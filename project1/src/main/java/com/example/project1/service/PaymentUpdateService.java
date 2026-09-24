package com.example.project1.service;

import com.example.project1.model.Order;
import com.example.project1.model.OrderItem;
import com.example.project1.model.Product;
import com.example.project1.repositories.OrderRepository;
import com.example.project1.repositories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PaymentUpdateService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public PaymentUpdateService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Async
    @Transactional
    public void updateOrderStatus(Long orderId)
    {
        Order order = orderRepository.findById(orderId).orElseThrow(()->new EntityNotFoundException("order"));

        order.setOrderStatus(Order.OrderStatus.PAID);

        List<OrderItem> items = order.getOrderItems();
        List<Product> products = items.stream().map(OrderItem::getProduct).toList();
        int i=0;
        for(Product product: products)
        {
            OrderItem item = items.get(i);
            Integer stock = product.getStock()- item.getQuantity();
            product.setStock(stock);
            i++;
        }

        productRepository.saveAll(products);
        orderRepository.save(order);
        log.info("order and product status was updated after payment success");
    }
}
