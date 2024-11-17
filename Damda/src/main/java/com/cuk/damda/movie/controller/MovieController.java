package com.cuk.damda.movie.controller;

import com.cuk.damda.global.controller.ApiResponse;
import com.cuk.damda.movie.controller.response.MovieDetailsResponse;
import com.cuk.damda.movie.controller.response.MovieListResponse;
import com.cuk.damda.movie.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
@Tag(name = "영화 컨트롤러")
@RequestMapping("/movie")
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/search")
    @Operation(summary = "외부 api에서 제목으로 영화 검색 ",description = "컨텐츠 등록 전 등록할 영화 검색: 제목, 페이지 값 필수")
    public ApiResponse<List<MovieListResponse>> searchMovieList(@RequestParam("title") String title, @RequestParam("page") int page){
        return ApiResponse.ok(movieService.searchMovieList(title, page));
    }

    @PostMapping("/{apiId}")
    @Operation(summary = "컨텐츠에 영화 등록 ",description = "searchMovieList에서 선택한 영화의 apiId를 이용해 영화 등록")
    public ApiResponse<String> addMovie(@PathVariable int apiId, @AuthenticationPrincipal UserDetails user){
        movieService.addMovieContents(apiId, user.getUsername());
        return ApiResponse.of(HttpStatus.CREATED, "success");
    }

    @GetMapping("/list")
    @Operation(summary = "영화 상세정보 리스트",description = "다른 사람 홈 검색 시 검색할 영화 정보")
    public ApiResponse<Page<MovieDetailsResponse>> getMovieDetailsList(
            @RequestParam String title,
            @PageableDefault(size = 10) Pageable pageable
    ){
        return ApiResponse.ok(movieService.getMovieDetailsList(title, pageable));
    }
}
