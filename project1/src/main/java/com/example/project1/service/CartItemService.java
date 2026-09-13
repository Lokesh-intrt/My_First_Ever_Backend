package com.example.project1.service;

import com.example.project1.DTOs.CartItemModifyDTO;
import com.example.project1.DTOs.CartItemRequestDTO;
import com.example.project1.DTOs.CartItemResponseDTO;
import com.example.project1.exceptions.IllegalQuantityException;
import com.example.project1.exceptions.ResourceNotFoundException;
import com.example.project1.mappers.MapCartItemResponseDTO;
import com.example.project1.model.Cart;
import com.example.project1.model.CartItem;
import com.example.project1.model.Product;
import com.example.project1.repositories.CartItemRepository;
import com.example.project1.repositories.CartRepository;
import com.example.project1.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final CartService cartService;
    private final MapCartItemResponseDTO mapCartItemResponseDTO;

    public CartItemService(CartRepository cartRepository, ProductRepository productRepository, CartItemRepository cartItemRepository, @Lazy CartService cartService, MapCartItemResponseDTO mapCartItemResponseDTO) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
        this.cartService = cartService;
        this.mapCartItemResponseDTO = mapCartItemResponseDTO;
    }

    @Transactional
    public CartItem createCartItem(CartItemRequestDTO cartItemRequestDTO)
    {
        Product product = productRepository.findById(cartItemRequestDTO.getProductId()).orElseThrow(()->new ResourceNotFoundException("product", cartItemRequestDTO.getProductId()));

        if(product.getStock()<cartItemRequestDTO.getQuantity())
            throw new IllegalQuantityException(product.getName());
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(cartItemRequestDTO.getQuantity());
        cartItemRepository.save(cartItem);
        return cartItem;
    }

    @Transactional
    public CartItemResponseDTO modifyCartItem(CartItemModifyDTO cartItemModifyDTO)
    {
        CartItem cartItem = cartItemRepository.findById(cartItemModifyDTO.getCartItemId()).orElseThrow(()->new ResourceNotFoundException("cartItem"));

        cartItem.setQuantity(cartItemModifyDTO.getNewQuantity());
        cartItemRepository.save(cartItem);
        Cart cart = cartRepository.findById(cartItem.getCart().getCartId()).orElseThrow(()->new ResourceNotFoundException("cart"));
        cartService.modifyCart(cart);

        return mapCartItemResponseDTO.toDTO(cartItem);
    }

    @Transactional
    public void removeCartItem(Long cartId)
    {
        CartItem cartItem = cartItemRepository.findById(cartId).orElseThrow(()->new ResourceNotFoundException("cartItem"));

        Cart cart = cartRepository.findById(cartItem.getCart().getCartId()).orElseThrow(()->new ResourceNotFoundException("cart"));
        cart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);

        cartItem.setCart(null);
        cartService.modifyCart(cart);
    }
}
