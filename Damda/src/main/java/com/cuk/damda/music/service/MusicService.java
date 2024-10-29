package com.cuk.damda.music.service;

import com.cuk.damda.music.controller.dto.ManiaDBDTO;
import com.cuk.damda.music.controller.dto.MusicSearchReq;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface MusicService {
    List<ManiaDBDTO> searchToManiaDB(MusicSearchReq musicSearchReq);
}
