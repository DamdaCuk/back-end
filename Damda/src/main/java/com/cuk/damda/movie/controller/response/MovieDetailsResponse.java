package com.cuk.damda.movie.controller.response;

import com.cuk.damda.movie.domain.Movie;

public record MovieDetailsResponse(
        Long movieId,
        String title,
        String posterPath,
        String director,
        String actor,
        String genre
) {
    public static MovieDetailsResponse of(Movie movie){
        return new MovieDetailsResponse(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getPoster(),
                movie.getDirector(),
                movie.getActor(),
                movie.getGenre()
        );
    }
}
