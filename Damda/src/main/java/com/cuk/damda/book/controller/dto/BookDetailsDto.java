package com.cuk.damda.book.controller.dto;

public record BookDetailsDto(
        String isbn,
        String title,       // 제목
        String author,      // 작가
        String publisher,   // 출판사
        String image      // 이미지
) {
    public static BookDetailsDto from(String isbn, String title, String author, String publisher, String image) {
        return new BookDetailsDto(
                isbn,
                title,
                author,
                publisher,
                image
        );
    }
}

