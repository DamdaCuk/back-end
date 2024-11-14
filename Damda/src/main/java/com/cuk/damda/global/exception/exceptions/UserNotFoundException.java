package com.cuk.damda.global.exception.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("유저를 찾을 수 없음");
    }
}
