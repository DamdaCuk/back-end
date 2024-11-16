package com.cuk.damda.movie.controller.response;

public record MovieApiResponse(
        String title,
        String posterPath,
        String director,
        String actor,
        String genre
) {
    public static MovieApiResponse from(String title, String posterPath, String director, String actor, String genre) {
        return new MovieApiResponse(
                title,
                posterPath,
                director,
                actor,
                genre
        );
    }
}
