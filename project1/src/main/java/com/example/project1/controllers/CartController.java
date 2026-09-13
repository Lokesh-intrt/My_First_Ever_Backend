package com.example.project1.controllers;

import com.example.project1.DTOs.CartRequestDTO;
import com.example.project1.DTOs.CartResponseDTO;
import com.example.project1.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/create")
    public ResponseEntity<CartResponseDTO> createCart(@Valid @RequestBody CartRequestDTO cartRequestDTO, Principal principal)
    {
        return ResponseEntity.ok(cartService.createCart(cartRequestDTO, principal));
    }

    @DeleteMapping("/clear/{cartId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long cartId)
    {
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }
}
