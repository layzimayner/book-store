package com.example.demo.repository;

import com.example.demo.model.Book;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.Optional;

@DataJpaTest
@Testcontainers
@Sql(scripts = "classpath:database/scripts/insert-books-and-categories.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:database/scripts/cleanup-db.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Check if book exists by real ISBN")
    void existsByIsbn_RealIsbn_ReturnBook() {
        String realIsbn = "1234567890123";
        boolean actual = bookRepository.existsByIsbn(realIsbn);
        Assertions.assertTrue(actual);
    }

    @Test
    @DisplayName("Find all books by valid category ID")
    void findAllByCategoryId_validId_ReturnListOfBooks() {
        int actual = bookRepository.findAllByCategoryId(2L).size();
        int expected = 2;
        Assertions.assertEquals(actual,expected);
    }

    @Test
    @DisplayName("Find book by ID with categories")
    void findByIdWithCategories_validBook_ReturnInitBook() {
        Optional<Book> actual = bookRepository.findByIdWithCategories(1L);
        Assertions.assertNotNull(actual);
    }
}
