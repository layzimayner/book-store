package com.example.demo.service;

import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDtoDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    void delete(Long id);

    BookDto update(Long id, CreateBookRequestDto requestDto);
}
