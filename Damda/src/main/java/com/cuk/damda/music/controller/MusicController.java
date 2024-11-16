package com.cuk.damda.music.controller;

import com.cuk.damda.book.controller.response.BookDetailsResponse;
import com.cuk.damda.global.controller.ApiResponse;
import com.cuk.damda.music.controller.dto.ManiaDBDTO;
import com.cuk.damda.music.controller.request.MusicSearchRequest;
import com.cuk.damda.music.controller.response.MusicDetailsResponse;
import com.cuk.damda.music.service.MusicService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/music")
public class MusicController {

    private final MusicService musicService;

    @PostMapping("/search")
    public ApiResponse<List<ManiaDBDTO>> searchMusic(@RequestBody MusicSearchRequest musicSearchRequest) {
        List<ManiaDBDTO> musics = musicService.searchToManiaDB(musicSearchRequest);
        return ApiResponse.ok(musics);
    }

    @PostMapping
    public ApiResponse<String> addMusic(@RequestBody ManiaDBDTO maniaDBDTO) {
        musicService.addMusicContents(maniaDBDTO);
        return ApiResponse.of(HttpStatus.CREATED, "success");
    }

    @GetMapping("/list")
    public ApiResponse<Page<MusicDetailsResponse>> getMusicDetailsList(
            @RequestParam String title,
            @PageableDefault(size = 10) Pageable pageable
    ){
        return ApiResponse.ok(musicService.getMusicDetailsList(title, pageable));
    }

}
