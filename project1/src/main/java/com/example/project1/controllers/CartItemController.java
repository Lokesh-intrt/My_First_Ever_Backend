package com.example.project1.controllers;

import com.example.project1.DTOs.CartItemModifyDTO;
import com.example.project1.DTOs.CartItemRequestDTO;
import com.example.project1.DTOs.CartItemResponseDTO;
import com.example.project1.service.CartItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cartItem")
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PatchMapping("/modify")
    public ResponseEntity<CartItemResponseDTO> modifyCartItem(@RequestBody CartItemModifyDTO cartItemModifyDTO)
    {
        return ResponseEntity.ok(cartItemService.modifyCartItem(cartItemModifyDTO));
    }

    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<Void> removeCartItem(@PathVariable Long cartItemId)
    {
        cartItemService.removeCartItem(cartItemId);
        return ResponseEntity.noContent().build();
    }
}
