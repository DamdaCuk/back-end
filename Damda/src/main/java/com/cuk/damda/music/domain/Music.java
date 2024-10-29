package com.cuk.damda.music.domain;

import com.cuk.damda.global.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Table(name="movie_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Music extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long musicId;
    private Integer apiId;
    private String title;
    private String artist;
    private String album;
    private LocalDate releaseDate;
    private String albumCover;

    @Builder
    public Music(Integer apiId, String title, String artist, String album, LocalDate releaseDate, String albumCover) {
        this.apiId = apiId;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.releaseDate = releaseDate;
        this.albumCover = albumCover;
    }

    public static Music create(Integer apiId, String title, String artist, String album, LocalDate releaseDate, String albumCover) {
        return Music.builder()
                .apiId(apiId)
                .title(title)
                .artist(artist)
                .album(album)
                .releaseDate(releaseDate)
                .albumCover(albumCover)
                .build();
    }
}
