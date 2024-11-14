package com.cuk.damda.global.exception.exceptions;

public class GuestbookNotFoundException extends RuntimeException {
    public GuestbookNotFoundException() {
        super("방명록을 찾을 수 없습니다.");
    }
}
