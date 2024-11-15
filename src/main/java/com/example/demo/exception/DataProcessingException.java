package com.example.demo.exception;

public class DataProcessingException extends RuntimeException {
    public DataProcessingException(String massage, Exception e) {
        super(massage, e);
    }
}
