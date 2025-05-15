package com.bookserve.exception;

public class CategoryHasBooksException extends RuntimeException {
    public CategoryHasBooksException(String message) {
        super(message);
    }
}