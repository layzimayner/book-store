package com.example.demo;

import com.example.demo.model.Book;
import com.example.demo.service.BookService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    public CommandLineRunner commandLineRunner() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Book book = new Book();
                book.setAuthor("Bob Alice");
                book.setCoverImage("Transaction");
                book.setIsbn("isbn");
                book.setPrice(BigDecimal.valueOf(9.9));
                book.setTitle("Transaction");
                book.setDescription("Transaction");
                bookService.save(book);
                System.out.println(bookService.findAll());
            }
        };
    }
}

