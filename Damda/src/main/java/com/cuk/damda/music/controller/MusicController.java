package com.cuk.damda.music.controller;

import com.cuk.damda.global.controller.ApiResponse;
import com.cuk.damda.music.controller.dto.ManiaDBDTO;
import com.cuk.damda.music.controller.dto.MusicSearchReq;
import com.cuk.damda.music.service.MusicService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/music")
public class MusicController {

    private final MusicService musicService;

    @PostMapping("/search")
    public ApiResponse<?> searchMusic(@RequestBody MusicSearchReq musicSearchReq) {
        List<ManiaDBDTO> musics = musicService.searchToManiaDB(musicSearchReq);
        return ApiResponse.ok(musics);
    }
}
