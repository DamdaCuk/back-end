package com.cuk.damda.book.domain;

import com.cuk.damda.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@ToString
@Table(name = "book_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Book extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId; // 고유한 책 ID (기본 키)
    private String isbn;
    private String title;       // 제목
    private String author;      // 작가
    private String publisher;   // 출판사 정보
    private String image;       // 이미지
    //private String description; // 줄거리

    @Builder
    public Book(String isbn, String title, String author, String publisher, String image) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher; // 출판사 정보 설정
        this.image = image;
        //this.description = description;
    }

    public static Book create(String isbn, String title, String author, String publisher, String image) {
        return Book.builder()
                .isbn(isbn)
                .title(title)
                .author(author)
                .publisher(publisher) // 출판사 정보 설정
                .image(image)
                //.description(description)
                .build();
    }
}


