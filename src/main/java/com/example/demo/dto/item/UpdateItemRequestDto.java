package com.example.demo.dto.item;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateItemRequestDto {
    @Positive(message = "Quantity must be at least 1")
    private int quantity;
}
