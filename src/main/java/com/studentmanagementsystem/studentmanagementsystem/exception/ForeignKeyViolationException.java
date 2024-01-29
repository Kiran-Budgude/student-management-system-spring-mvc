package com.studentmanagementsystem.studentmanagementsystem.exception;

public class ForeignKeyViolationException extends RuntimeException {
    public ForeignKeyViolationException(String message) {
        super(message);
    }
}

