package com.example.demo.dto.category;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;

public class CreateCategoryRequestDto {
    @NotBlank
    @Max(value = 50, message = "Length of category must by shorter")
    private String name;
    private String description;
}
