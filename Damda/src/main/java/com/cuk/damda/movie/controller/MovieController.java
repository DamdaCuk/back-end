package com.cuk.damda.movie.controller;

import com.cuk.damda.global.controller.ApiResponse;
import com.cuk.damda.movie.controller.response.MovieDetailsResponse;
import com.cuk.damda.movie.controller.response.MovieListResponse;
import com.cuk.damda.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/movie")
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/search")
    public ApiResponse<List<MovieListResponse>> searchMovieList(@RequestParam("title") String title, @RequestParam("page") int page){
        return ApiResponse.ok(movieService.searchMovieList(title, page));
    }

    @PostMapping("/{apiId}")
    public ApiResponse<String> addMovie(@PathVariable int apiId){
        movieService.addMovieContents(apiId);
        return ApiResponse.of(HttpStatus.CREATED, "success");
    }

    @GetMapping("/list")
    public ApiResponse<Page<MovieDetailsResponse>> getMovieDetailsList(
            @RequestParam String title,
            @PageableDefault(size = 10) Pageable pageable
    ){
        return ApiResponse.ok(movieService.getMovieDetailsList(title, pageable));
    }
}
