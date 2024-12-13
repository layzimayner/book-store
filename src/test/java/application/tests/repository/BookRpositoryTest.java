package application.tests.repository;

import com.example.demo.repository.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("""
            
            """)
    @Sql(scripts = {
            "classpath:database/books/add-book.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
    void existsByIsbn_RealIsbn_ReturnBook
}
