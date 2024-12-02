package com.example.demo.repository;

import com.example.demo.model.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByIsbn(String isbn);

    @Query("SELECT b FROM Book b JOIN b.categories c WHERE c.id = :categoryId")
    List<Book> findAllByCategoryId(@Param("categoryId") Long categoryId);
}
