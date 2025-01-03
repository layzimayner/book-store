package com.example.demo.dto.cart;

import com.example.demo.model.CartItem;
import java.util.Set;
import lombok.Data;

@Data
public class CartDto {
    private Long id;
    private Long userId;
    private Set<CartItem> items;
}
