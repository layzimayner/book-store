package com.example.demo.dto.book;

import com.example.demo.annotation.unique.Unique;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreateBookRequestDto {
    @NotBlank
    private String title;
    @NotBlank
    private String author;
    @NotBlank
    @Unique
    private String isbn;
    @NotBlank
    @Positive
    private BigDecimal price;
    private String description;
    private String coverImage;
}
