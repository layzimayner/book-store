package com.example.demo.service;

import com.example.demo.model.Book;
import java.util.List;

public interface BookService {
    public Book save(Book book);

    public List<Book> findAllBook();
}
