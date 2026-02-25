package com.rajendra.expensemanager.exception;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class ApiResponse<T> {

    private final boolean success;
    private final String message;
    private final T data;
    private final Map<String, String> errors;
    private final LocalDateTime timestamp;
    private final String path;

    public ApiResponse(boolean success, String message, T data,
                       Map<String, String> errors, String path) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
        this.path = path;
    }
}
