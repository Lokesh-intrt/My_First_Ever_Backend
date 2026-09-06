package com.example.project1.DTOs;

import com.example.project1.model.OrderItem;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {

    @NotNull
    private Long orderId;

    @NotNull
    @DecimalMin(value = "0",message = "This field cant be less than zero")
    private Double totalAmount;

    @NotNull
    private List<OrderItemResponseDTO> orderItemResponse;

    @NotNull
    @NotBlank
    private String userName;
}
