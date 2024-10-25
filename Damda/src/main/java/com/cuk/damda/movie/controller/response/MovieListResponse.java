package com.cuk.damda.movie.controller.response;

public record MovieListResponse(
        int apiId,
        String title,
        String posterPath

) {
    public static MovieListResponse from(int apiId, String title, String posterPath) {
        return new MovieListResponse(
                apiId,
                title,
                posterPath
        );
    }
}
