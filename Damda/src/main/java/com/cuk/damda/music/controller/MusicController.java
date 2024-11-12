package com.cuk.damda.music.controller;

import com.cuk.damda.global.controller.ApiResponse;
import com.cuk.damda.music.controller.dto.ManiaDBDTO;
import com.cuk.damda.music.controller.request.MusicSearchRequest;
import com.cuk.damda.music.service.MusicService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/music")
public class MusicController {

    private final MusicService musicService;

    @GetMapping("/search")
    public ApiResponse<List<ManiaDBDTO>> searchMusic(@RequestBody MusicSearchRequest musicSearchRequest) {
        List<ManiaDBDTO> musics = musicService.searchToManiaDB(musicSearchRequest);
        return ApiResponse.ok(musics);
    }

    @PostMapping
    public ApiResponse<String> addMusic(@RequestBody ManiaDBDTO maniaDBDTO) {
        musicService.addMusicContents(maniaDBDTO);
        return ApiResponse.of(HttpStatus.CREATED, "success");
    }

    @DeleteMapping("/{contentsId}")
    public ApiResponse<Long> deleteMusic(@PathVariable Long contentsId){
        musicService.deleteMusicContents(contentsId);
        return ApiResponse.ok(contentsId);
    }
}
