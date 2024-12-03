package com.example.demo.mapper;

import com.example.demo.dto.cart.CartDto;
import com.example.demo.model.ShoppingCart;

public interface CartMapper {
    CartDto toDto(ShoppingCart soppingCart);
}
