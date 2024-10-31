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

    private String title;
    private String author;
    private String image;
    private String description;

    @Builder
    public Book(String title, String author, String image, String description) {
        this.title = title;
        this.author = author;
        this.image = image;
        this.description = description;
    }

    public static Book create(String title, String author, String image, String description) {
        return Book.builder()
                .title(title)
                .author(author)
                .image(image)
                .description(description)
                .build();
    }
}
