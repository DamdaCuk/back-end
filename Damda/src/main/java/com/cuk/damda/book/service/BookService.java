package com.cuk.damda.book.service;

import com.cuk.damda.book.controller.dto.BookDetailsDto;
import java.util.List;

public interface BookService {
    List<BookDetailsDto> searchBookByTitle(String title, int page);
    void addBookContents(Long homeId, BookDetailsDto bookDto);
}


