package com.example.demo.dto.item;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateItemRequestDro {
    @Positive
    @NotNull
    private Long bookId;
    @Positive(message = "Quantity must be at least 1")
    private int quantity;
}
