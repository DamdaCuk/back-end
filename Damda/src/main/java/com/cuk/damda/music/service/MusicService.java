package com.cuk.damda.music.service;

import com.cuk.damda.music.controller.dto.ManiaDBDTO;
import com.cuk.damda.music.controller.request.MusicSearchRequest;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface MusicService {
    List<ManiaDBDTO> searchToManiaDB(MusicSearchRequest musicSearchRequest);
    void addMusicContents(ManiaDBDTO maniaDBDTO);
}
