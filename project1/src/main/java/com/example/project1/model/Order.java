package com.example.project1.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor(force = true)
@Entity
@Table(name = "orders")
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Version
    private Integer version;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double totalAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public enum OrderStatus{
        EN_ROUTE,
        DELIVERED,
        REROUTING,
        DELAYED,
        FAILED,
        PAID
    }

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,orphanRemoval = true,fetch=FetchType.LAZY)
    private List<OrderItem> orderItems;
}
