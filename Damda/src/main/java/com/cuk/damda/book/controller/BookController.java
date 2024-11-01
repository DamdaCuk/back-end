package com.cuk.damda.book.controller;

import com.cuk.damda.global.controller.ApiResponse;
import com.cuk.damda.book.controller.response.BookListResponse;
import com.cuk.damda.book.service.BookServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookServiceImpl bookService;

    @GetMapping("/search")
    public ApiResponse<List<BookListResponse>> searchBook(@RequestParam("title") String title, @RequestParam("page") int page) {
        List<BookListResponse> bookList = bookService.searchBookByTitle(title, page);
        return ApiResponse.ok(bookList);
    }
}





