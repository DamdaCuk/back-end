package com.cuk.damda.movie.repository;

import com.cuk.damda.movie.domain.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findByApiId(int apiId);
    Page<Movie> findByTitleContains(String title, Pageable pageable);
}
