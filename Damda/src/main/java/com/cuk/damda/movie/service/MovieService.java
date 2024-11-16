package com.cuk.damda.movie.service;

import com.cuk.damda.movie.controller.response.MovieDetailsResponse;
import com.cuk.damda.movie.controller.response.MovieListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MovieService {
    List<MovieListResponse> searchMovieList(String title, int page);
    void addMovieContents(int apiId);
    Page<MovieDetailsResponse> getMovieDetailsList(String title, Pageable pageable);
}
