package com.example.demo.controller;

import com.example.demo.dto.BookDto;
import com.example.demo.dto.CreateBookRequestDto;
import com.example.demo.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.awt.print.Pageable;
import java.util.List;

@Tag(name = "product management", description = "Endpoints for management products")
@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {
    private BookService bookService;

    @GetMapping
    @Operation(summary = "Get all books", description = "Get a list of all available books")
    public List<BookDto> getAllBooks(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete book", description = "Delete book by id")
    public void deleteBook(@PathVariable Long id) {
        bookService.delete(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update book", description = "Delete book by id")
    public BookDto updateBook(@PathVariable Long id, CreateBookRequestDto requestDto){
        return bookService.update(id, requestDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by id", description = "Get book by id")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Create new book", description = "Add new book to DB")
    public BookDto createBook(CreateBookRequestDto requestDto) {
        return bookService.save(requestDto);
    }

}
