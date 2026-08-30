package com.example.project1.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor(force = true)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id" , nullable = false)
    private User seller;

    @Column(nullable = false)
    private Integer stock;

    public enum ProductStatus{
        OUTOFSTOCK,
        AVAILABLE,
        NOT_AVAILABLE
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;
}
