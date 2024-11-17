package com.cuk.damda.book.service;

import com.cuk.damda.book.controller.dto.BookDetailsDto;
import com.cuk.damda.book.controller.response.BookDetailsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {
    List<BookDetailsDto> searchBookByTitle(String title, int page);
    void addBookContents(BookDetailsDto bookDto, String userEmail);
    Page<BookDetailsResponse> getBookDetailsList(String title, Pageable pageable);
}


