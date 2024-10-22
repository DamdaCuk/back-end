package com.cuk.damda.movie.controller;

import com.cuk.damda.global.controller.ApiResponse;
import com.cuk.damda.movie.controller.response.MovieDetailsResponse;
import com.cuk.damda.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/movie")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @GetMapping("/search/{movieId}")
    public ApiResponse<MovieDetailsResponse> getMovieDetails(@PathVariable int movieId){
        //일단 무슨 타입으로 오는지 모르니까
        MovieDetailsResponse movieDetailsResponse = movieService.getMovieDetails(movieId);
        return ApiResponse.ok(movieDetailsResponse);
    }
}
