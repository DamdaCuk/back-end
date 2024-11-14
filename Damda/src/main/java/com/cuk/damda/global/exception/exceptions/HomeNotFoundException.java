package com.cuk.damda.global.exception.exceptions;

public class HomeNotFoundException extends RuntimeException {
    public HomeNotFoundException() {
        super("홈을 찾을 수 없음");
    }
}
