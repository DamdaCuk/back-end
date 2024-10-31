package com.cuk.damda.movie.controller;

import com.cuk.damda.global.controller.ApiResponse;
import com.cuk.damda.movie.controller.response.MovieDetailsResponse;
import com.cuk.damda.movie.controller.response.MovieListResponse;
import com.cuk.damda.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/movie")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @GetMapping("/search")
    public ApiResponse<List<MovieListResponse>> getMovieList(@RequestParam("title") String title, @RequestParam("page") int page){
        return ApiResponse.ok(movieService.getMovieList(title, page));
    }

    @PostMapping("/{apiId}")
    public ApiResponse<String> addMovie(@PathVariable int apiId){
        movieService.addMovieContents(apiId);
        return ApiResponse.of(HttpStatus.CREATED, "success");
    }

    @DeleteMapping("/{contentsId}")
    public ApiResponse<Long> deleteMovie(@PathVariable Long contentsId){
        movieService.deleteMovieContents(contentsId);
        return ApiResponse.ok(contentsId);
    }
}
