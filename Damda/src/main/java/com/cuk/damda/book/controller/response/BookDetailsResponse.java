package com.cuk.damda.book.controller.response;

import com.cuk.damda.book.domain.Book;

public record BookDetailsResponse(
        Long bookId,
        String title,
        String author,
        String publisher,
        String image
) {
    public static BookDetailsResponse of(Book book) {
        return new BookDetailsResponse(
                book.getBookId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublisher(),
                book.getImage()
        );
    }
}
