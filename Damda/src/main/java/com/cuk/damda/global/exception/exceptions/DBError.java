package com.cuk.damda.global.exception.exceptions;

public class DBError extends RuntimeException {
    public DBError(String message) {
        super(message);
    }
}
