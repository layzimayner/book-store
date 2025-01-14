package com.example.demo.dto.item;

import lombok.Data;

@Data
public class OrderItemDto {
    private Long orderItemId;
    private Long bookId;
    private int quantity;
}
