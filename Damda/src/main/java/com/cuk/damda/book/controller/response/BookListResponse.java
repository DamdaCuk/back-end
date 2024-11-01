package com.cuk.damda.book.controller.response;

public record BookListResponse(
        String title,       // 제목
        String author,      // 작가
        String publisher,   // 출판사
        String image,       // 이미지
        String description   // 줄거리
) {
}

