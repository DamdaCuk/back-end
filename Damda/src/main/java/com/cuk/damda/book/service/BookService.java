package com.cuk.damda.book.service;

import com.cuk.damda.book.controller.response.BookListResponse;
import java.util.List;

public interface BookService {
    List<BookListResponse> searchBookByTitle(String title);
}


