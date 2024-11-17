package com.cuk.damda.music.controller.response;

import com.cuk.damda.music.domain.Music;

public record MusicDetailsResponse(
        Long musicId,
        String title,
        String artist,
        String album,
        String album_cover
) {
    public static MusicDetailsResponse of(Music music){
        return new MusicDetailsResponse(
                music.getMusicId(),
                music.getTitle(),
                music.getArtist(),
                music.getAlbum(),
                music.getAlbumCover()
        );
    }
}
