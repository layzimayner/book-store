package com.example.demo.dto.cart;

import com.example.demo.dto.item.CartItemDto;
import java.util.Set;
import lombok.Data;

@Data
public class CartDto {
    private Long id;
    private Set<CartItemDto> cartItems;
}
