package com.cuk.damda.movie.controller;

import com.cuk.damda.global.controller.ApiResponse;
import com.cuk.damda.movie.controller.response.MovieDetailsResponse;
import com.cuk.damda.movie.controller.response.MovieListResponse;
import com.cuk.damda.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/movie")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @GetMapping("/search/{movieId}")
    public ApiResponse<MovieDetailsResponse> getMovieDetails(@PathVariable int movieId){
        MovieDetailsResponse movieDetailsResponse = movieService.getMovieDetails(movieId);
        return ApiResponse.ok(movieDetailsResponse);
    }

    @GetMapping("/search")
    public ApiResponse<List<MovieListResponse>> getMovieList(@RequestParam("title") String title, @RequestParam("page") int page){
        return ApiResponse.ok(movieService.getMovieList(title, page));
    }
}
