package com.example.demo.service;

import com.example.demo.dto.cart.CartDto;
import com.example.demo.dto.item.CreateItemRequestDro;
import com.example.demo.dto.item.UpdateItemRequestDto;
import org.springframework.security.core.Authentication;

public interface CartService {
    CartDto getCart(Authentication authentication);

    CartDto addItemToCart(CreateItemRequestDro requestDro,
                          Authentication authentication);

    CartDto updateItemQuantity(Long id,
                               UpdateItemRequestDto requestDro,
                               Authentication authentication);

    void deleteItemFromCart(Long id,
                            Authentication authentication);
}
