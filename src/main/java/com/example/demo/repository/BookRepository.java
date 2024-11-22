package com.example.demo.repository;

import com.example.demo.dto.BookDto;
import com.example.demo.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import java.awt.print.Pageable;

public interface BookRepository extends JpaRepository<Book, Long> {
    Page<BookDto> findAll(Pageable pageable);
}
