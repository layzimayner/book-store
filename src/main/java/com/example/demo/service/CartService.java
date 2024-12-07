package com.example.demo.service;

import com.example.demo.dto.cart.CartDto;
import com.example.demo.dto.item.CreateCartItemRequestDro;
import com.example.demo.dto.item.UpdateCartItemRequestDto;
import com.example.demo.model.User;

public interface CartService {
    CartDto getCart(Long userId);

    CartDto addItemToCart(CreateCartItemRequestDro requestDro,
                          User user);

    CartDto updateItemQuantity(Long id,
                               UpdateCartItemRequestDto requestDro,
                               User user);

    void deleteItemFromCart(Long id,
                            User user);

    void createShoppingCartForUser(User user);
}
