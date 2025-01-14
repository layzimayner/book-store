package com.example.demo.service;

import com.example.demo.dto.cart.CartDto;
import com.example.demo.dto.item.CartItemDto;
import com.example.demo.dto.item.CreateCartItemRequestDto;
import com.example.demo.dto.item.UpdateCartItemRequestDto;
import com.example.demo.model.User;

public interface CartService {
    CartDto getCart(Long userId);

    CartItemDto addItemToCart(CreateCartItemRequestDto requestDro,
                              User user);

    CartItemDto updateItemQuantity(Long id,
                                              UpdateCartItemRequestDto requestDro,
                                              User user);

    void deleteItemFromCart(Long id,
                            User user);

    void createShoppingCartForUser(User user);
}
