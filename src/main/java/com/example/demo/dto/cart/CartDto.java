package com.example.demo.dto.cart;

import com.example.demo.dto.item.CartItemDto;
import java.util.Set;
import lombok.Data;

@Data
public class CartDto {
    private Long userId;
    private Long cartId;
    private Set<CartItemDto> cartItems;
}
