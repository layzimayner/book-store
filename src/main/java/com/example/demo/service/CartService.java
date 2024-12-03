package com.example.demo.service;

import com.example.demo.dto.cart.CartDto;
import com.example.demo.dto.item.CreateItemRequestDro;
import com.example.demo.dto.item.UpdateItemRequestDto;
import com.example.demo.model.User;

public interface CartService {
    CartDto getCart(Long userId);

    CartDto addItemToCart(CreateItemRequestDro requestDro,
                          User user);

    CartDto updateItemQuantity(Long id,
                               UpdateItemRequestDto requestDro,
                               User user);

    void deleteItemFromCart(Long id,
                            User user);
}
