package com.cuk.damda.movie.controller.response;

import java.util.List;

public record MovieDetailsResponse(
        String title,
        String posterPath,
        String director,
        String actor,
        String genre
) {
    public static MovieDetailsResponse from(String title, String posterPath, String director, String actor, String genre) {
        return new MovieDetailsResponse(
                title,
                posterPath,
                director,
                actor,
                genre
        );
    }
}
