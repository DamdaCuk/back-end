package com.cuk.damda.movie.service;

import com.cuk.damda.movie.controller.response.MovieDetailsResponse;

public interface MovieService {
    MovieDetailsResponse getMovieDetails(int movieId);
}
