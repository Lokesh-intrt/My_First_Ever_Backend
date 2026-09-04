package com.example.project1.DTOs;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderItemRequestDTO {

    private Long id;
    private Integer quantity;
}
