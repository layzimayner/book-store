package com.example.demo.service;

import com.example.demo.dto.BookDto;
import com.example.demo.dto.CreateBookRequestDto;

import java.awt.print.Pageable;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDtoDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    void delete(Long id);

    BookDto update(Long id, CreateBookRequestDto requestDto);
}
