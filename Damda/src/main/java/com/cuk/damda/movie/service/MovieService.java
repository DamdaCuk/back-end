package com.cuk.damda.movie.service;

import com.cuk.damda.movie.controller.response.MovieDetailsResponse;
import com.cuk.damda.movie.controller.response.MovieListResponse;

import java.util.List;
import java.util.Optional;

public interface MovieService {
    List<MovieListResponse> getMovieList(String title, int page);
    void addMovieContents(int apiId);
    void deleteMovieContents(Long contentsId);
}
