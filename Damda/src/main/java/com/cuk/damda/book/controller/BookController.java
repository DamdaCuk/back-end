package com.cuk.damda.book.controller;

import com.cuk.damda.book.controller.response.BookDetailsResponse;
import com.cuk.damda.global.controller.ApiResponse;
import com.cuk.damda.book.controller.dto.BookDetailsDto;
import com.cuk.damda.book.service.BookServiceImpl;
import com.cuk.damda.movie.controller.response.MovieDetailsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "책 컨트롤러")
public class BookController {

    @Autowired
    private BookServiceImpl bookService;


    @GetMapping("/search")
    @Operation(summary = "외부 api에서 제목으로 책 찾기",description = "컨텐츠 등록 전 등록할 책 검색: 제목, 페이지 값 필수")
    public ApiResponse<List<BookDetailsDto>> searchBook(@RequestParam("title") String title, @RequestParam("page") int page) {
        List<BookDetailsDto> bookList = bookService.searchBookByTitle(title, page);
        return ApiResponse.ok(bookList);
    }

    @PostMapping
    @Operation(summary = "컨텐츠에 책 등록 ",description = "searchBook에서 검색된 json값을 이용해 등록")
    public ApiResponse<String> addBook(@RequestBody BookDetailsDto bookDetailsDto, @AuthenticationPrincipal
                                       UserDetails user) {
        bookService.addBookContents(bookDetailsDto, user.getUsername());
        return ApiResponse.of(HttpStatus.CREATED, "success");
    }

    @GetMapping("/list")
    @Operation(summary = "책 상세정보 리스트",description = "다른 사람 홈 검색 시 검색할 책 정보")
    public ApiResponse<Page<BookDetailsResponse>> getBookDetailsList(
            @RequestParam String title,
            @PageableDefault(size = 10) Pageable pageable
    ){
        return ApiResponse.ok(bookService.getBookDetailsList(title, pageable));
    }
}





