package com.example.demo.dto.item;

import lombok.Data;

@Data
public class CartItemResponseDto {
    private Long bookId;
    private int quantity;
}
