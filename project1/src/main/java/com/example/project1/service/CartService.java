package com.example.project1.service;

import com.example.project1.DTOs.CartItemRequestDTO;
import com.example.project1.DTOs.CartItemResponseDTO;
import com.example.project1.DTOs.CartRequestDTO;
import com.example.project1.DTOs.CartResponseDTO;
import com.example.project1.mappers.MapCartCartResponse;
import com.example.project1.model.Cart;
import com.example.project1.model.CartItem;
import com.example.project1.model.User;
import com.example.project1.repositories.CartRepository;
import com.example.project1.repositories.UserRepository;
import com.example.project1.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@PreAuthorize("isAuthenticated()")
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemService cartItemService;
    private final UserRepository userRepository;
    private final MapCartCartResponse mapCartCartResponse;

    public CartService(CartRepository cartRepository, CartItemService cartItemService, UserRepository userRepository, MapCartCartResponse mapCartCartResponse) {
        this.cartRepository = cartRepository;
        this.cartItemService = cartItemService;
        this.userRepository = userRepository;
        this.mapCartCartResponse = mapCartCartResponse;
    }

    @Transactional
    public CartResponseDTO createCart(CartRequestDTO cartRequestDTO, Principal principal)
    {
        int totalQuantity = 0;
        double totalPrice = 0.0;

        List<CartItem> cartItemList = new ArrayList<>();

        for(CartItemRequestDTO cartItemRequestDTO: cartRequestDTO.getCartItemRequestDTOS())
        {
            totalQuantity++;
            CartItem cartItem = cartItemService.createCartItem(cartItemRequestDTO);
            totalPrice += calculateTotal(cartItem.getQuantity(),cartItem.getProduct().getPrice());
            cartItemList.add(cartItem);
        }

        User user = userRepository.findByEmail(principal.getName()).orElseThrow(()->new ResourceNotFoundException("user"));
        Cart cart = Cart.builder().cartItems(cartItemList).totalAmount(totalPrice).user(user).totalQuantity(totalQuantity).build();
        cartItemList.forEach(cartItem -> cartItem.setCart(cart));

        cartRepository.save(cart);
        return mapCartCartResponse.toDTO(cart);
    }

    public static Double calculateTotal(int quantity, Double price)
    {
        return quantity*price;
    }

    @Transactional
    public void modifyCart(Cart cart) {

        int totalQuantity = 0;
        double totalPrice = 0.0;
        List<CartItem> cartItemList = new ArrayList<>();

        for(CartItem cartItem: cart.getCartItems())
        {
            totalQuantity++;
            totalPrice += calculateTotal(cartItem.getQuantity(),cartItem.getProduct().getPrice());
        }

        cart.setTotalAmount(totalPrice);
        cart.setTotalQuantity(totalQuantity);
    }

    @Transactional
    public void clearCart(Long cartId){

        Cart cart = cartRepository.findById(cartId).orElseThrow(()->new ResourceNotFoundException("cart"));

        cartRepository.delete(cart);
    }
}
