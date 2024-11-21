package com.example.demo.dto;

import com.example.demo.annotation.unique.Unique;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class BookDto {
    @NotBlank
    @Positive
    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String author;
    @NotBlank
    @Unique
    private String isbn;
    @Positive
    @NotBlank
    private BigDecimal price;
    private String description;
    private String coverImage;
}
