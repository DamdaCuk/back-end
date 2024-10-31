package com.cuk.damda.book.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookListResponse {
    private String title;
    private String author;
    private String image;
    private String description;
}
