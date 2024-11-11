package com.example.demo.repository;

import com.example.demo.model.Book;
import java.util.List;

public interface BookRepository {
    public Book save(Book book);

    public List<Book> findAll();
}
