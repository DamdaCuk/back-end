package com.cuk.damda.movie.service;

import com.cuk.damda.movie.controller.response.MovieDetailsResponse;
import com.cuk.damda.movie.controller.response.MovieListResponse;

import java.util.List;
import java.util.Optional;

public interface MovieService {
    MovieDetailsResponse getMovieDetails(int movieId);
    List<MovieListResponse> getMovieList(String title, int page);
}
