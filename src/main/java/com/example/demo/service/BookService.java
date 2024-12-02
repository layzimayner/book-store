package com.example.demo.service;

import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.BookDtoWithoutCategoryIds;
import com.example.demo.dto.book.CreateBookRequestDto;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDtoDto);

    Page<BookDtoWithoutCategoryIds> findAll(Pageable pageable);

    BookDto findById(Long id);

    void delete(Long id);

    BookDto update(Long id, CreateBookRequestDto requestDto);

    List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id);
}
