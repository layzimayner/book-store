package com.example.demo.dto.item;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class CreateItemRequestDro {
    @Min(value = 1, message = "Quantity must be at least 1")
    private Long bookId;
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
}
