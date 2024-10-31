package com.cuk.damda.music.controller.dto;

import java.time.LocalDate;

public record ManiaDBDTO(
    String apiId,
    String title,
    String artist,
    String album,
    LocalDate releaseDate,
    String albumCover
) {
    public static ManiaDBDTO from(String apiId, String title, String artist, String album, LocalDate releaseDate, String albumCover) {
        return new ManiaDBDTO(
                apiId,
                title,
                artist,
                album,
                releaseDate,
                albumCover
        );
    }
}
