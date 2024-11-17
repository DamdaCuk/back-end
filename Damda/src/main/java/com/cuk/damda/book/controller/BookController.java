package com.cuk.damda.book.controller;

import com.cuk.damda.book.controller.response.BookDetailsResponse;
import com.cuk.damda.global.controller.ApiResponse;
import com.cuk.damda.book.controller.dto.BookDetailsDto;
import com.cuk.damda.book.service.BookServiceImpl;
import com.cuk.damda.movie.controller.response.MovieDetailsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookServiceImpl bookService;

    @GetMapping("/search")
    public ApiResponse<List<BookDetailsDto>> searchBook(@RequestParam("title") String title, @RequestParam("page") int page) {
        List<BookDetailsDto> bookList = bookService.searchBookByTitle(title, page);
        return ApiResponse.ok(bookList);
    }

    @PostMapping
    public ApiResponse<String> addBook(@RequestBody BookDetailsDto bookDetailsDto, @AuthenticationPrincipal
                                       UserDetails user) {
        bookService.addBookContents(bookDetailsDto, user.getUsername());
        return ApiResponse.of(HttpStatus.CREATED, "success");
    }

    @GetMapping("/list")
    public ApiResponse<Page<BookDetailsResponse>> getBookDetailsList(
            @RequestParam String title,
            @PageableDefault(size = 10) Pageable pageable
    ){
        return ApiResponse.ok(bookService.getBookDetailsList(title, pageable));
    }
}





