package com.cuk.damda.home.controller.response;

public record HomeListResponse(
        Long homeId,
        String homeName,
        Long likes) {
    public static HomeListResponse from(Long homeId, String homeName, Long likes) {
        return new HomeListResponse(
                homeId,
                homeName,
                likes
        );
    }
}
