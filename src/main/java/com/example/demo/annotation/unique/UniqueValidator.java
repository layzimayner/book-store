package com.example.demo.annotation.unique;

import com.example.demo.repository.BookRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueValidator implements ConstraintValidator<Unique, String> {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        return !bookRepository.existsByIsbn(value);
    }
}
