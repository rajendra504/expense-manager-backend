package com.rajendra.expensemanager.exception;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
