package com.example.demo.controller;

import com.example.demo.dto.BookDto;
import com.example.demo.dto.CreateBookRequestDto;
import com.example.demo.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
@Validated
public class BookController {
    private BookService bookService;

    @GetMapping
    public List<BookDto> getAllBooks() {
        return bookService.findAll();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable @Valid Long id) {
        bookService.delete(id);
    }

    @PutMapping("/{id}")
    public BookDto updateBook(@PathVariable @Valid Long id, @Valid CreateBookRequestDto requestDto){
        return bookService.update(id, requestDto);
    }

    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable @Valid Long id) {
        return bookService.findById(id);
    }

    @PostMapping
    public BookDto createBook(@Valid CreateBookRequestDto requestDto) {
        return bookService.save(requestDto);
    }

}
