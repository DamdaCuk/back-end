package com.cuk.damda.music.service;

import com.cuk.damda.book.controller.response.BookDetailsResponse;
import com.cuk.damda.music.controller.dto.ManiaDBDTO;
import com.cuk.damda.music.controller.request.MusicSearchRequest;
import java.util.List;

import com.cuk.damda.music.controller.response.MusicDetailsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface MusicService {
    List<ManiaDBDTO> searchToManiaDB(MusicSearchRequest musicSearchRequest);
    void addMusicContents(ManiaDBDTO maniaDBDTO, String userEmail);
    Page<MusicDetailsResponse> getMusicDetailsList(String title, Pageable pageable);
}
