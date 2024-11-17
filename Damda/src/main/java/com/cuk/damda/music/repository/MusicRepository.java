package com.cuk.damda.music.repository;

import com.cuk.damda.music.domain.Music;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MusicRepository extends JpaRepository<Music, Long> {
    Optional<Music> findByApiId(String apiId);
    Page<Music> findByTitleContains(String title, Pageable pageable);
}
