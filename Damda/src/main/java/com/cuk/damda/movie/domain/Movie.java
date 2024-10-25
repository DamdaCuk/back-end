package com.cuk.damda.movie.domain;

import com.cuk.damda.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@ToString
@Table(name="movie_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Movie extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieId;
    private Integer apiId;
    private String director;
    private String actor;
    private String genre;
    private String title;
    private String poster;

    @Builder
    public Movie(Integer apiId, String director, String actor, String genre, String title, String poster) {
        this.apiId = apiId;
        this.director = director;
        this.actor = actor;
        this.genre = genre;
        this.title = title;
        this.poster = poster;
    }

    public static Movie create(Integer apiId, String director, String actor, String genre, String title, String poster) {
        return Movie.builder()
                .apiId(apiId)
                .director(director)
                .actor(actor)
                .genre(genre)
                .title(title)
                .poster(poster)
                .build();
    }
}
