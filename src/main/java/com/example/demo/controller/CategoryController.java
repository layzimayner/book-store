package com.example.demo.controller;

import com.example.demo.dto.book.BookDtoWithoutCategoryIds;
import com.example.demo.dto.category.CategoryDto;
import com.example.demo.dto.category.CreateCategoryRequestDto;
import com.example.demo.service.BookService;
import com.example.demo.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "categories management", description = "Endpoints for management categories")
@RestController
@RequestMapping("/categories")
@Validated
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final BookService bookService;

    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "Create new category", description = "Add new category to DB")
    public CategoryDto createCategory(@RequestBody @Valid CreateCategoryRequestDto requestDto) {
        return categoryService.save(requestDto);
    }

    @GetMapping
    @Operation(summary = "Get all categories", description = "Get list of all available categories")
    public List<CategoryDto> getAll(Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @GetMapping
    @Operation(summary = "Get category", description = "Get category by id")
    public CategoryDto getCategoryById(Long id) {
        return categoryService.getById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update category", description = "Update category by id")
    public CategoryDto updateCategory(
            @PathVariable @Positive Long id,
            @RequestBody @Valid CreateCategoryRequestDto categoryDto) {
        return categoryService.update(id, categoryDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category", description = "Delete category by id")
    public void deleteCategory(Long id) {
        categoryService.deleteById(id);
    }

    @GetMapping("/{categoryId}/books")
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(@RequestParam Long categoryId) {
        return bookService.getBooksByCategoryId(categoryId);
    }
}
