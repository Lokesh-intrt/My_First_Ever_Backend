package com.example.project1.DTOs;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderItemRequestDTO {

    @NotNull
    private Long id;

    @NotNull
    @Min(value = 1, message = "this field cant be zero")
    private Integer quantity;
}
