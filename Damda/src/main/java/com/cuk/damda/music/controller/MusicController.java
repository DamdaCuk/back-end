package com.cuk.damda.music.controller;

import com.cuk.damda.book.controller.response.BookDetailsResponse;
import com.cuk.damda.global.controller.ApiResponse;
import com.cuk.damda.music.controller.dto.ManiaDBDTO;
import com.cuk.damda.music.controller.request.MusicSearchRequest;
import com.cuk.damda.music.controller.response.MusicDetailsResponse;
import com.cuk.damda.music.service.MusicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/music")
@Tag(name = "음악 컨트롤러")
public class MusicController {

    private final MusicService musicService;

    @PostMapping("/search")
    @Operation(summary = "외부 api에서 제목으로 음악 찾기",description = "컨텐츠 등록 전 등록할 음악 검색: 제목, 타입")
    public ApiResponse<List<ManiaDBDTO>> searchMusic(@RequestBody MusicSearchRequest musicSearchRequest) {
        List<ManiaDBDTO> musics = musicService.searchToManiaDB(musicSearchRequest);
        return ApiResponse.ok(musics);
    }

    @PostMapping
    @Operation(summary = "컨텐츠에 음악 등록 ",description = "searchMusic에서 검색된 json값을 이용해 등록")
    public ApiResponse<String> addMusic(@RequestBody ManiaDBDTO maniaDBDTO, @AuthenticationPrincipal UserDetails user) {
        musicService.addMusicContents(maniaDBDTO, user.getUsername());
        return ApiResponse.of(HttpStatus.CREATED, "success");
    }

    @GetMapping("/list")
    @Operation(summary = "음악 상세정보 리스트",description = "다른 사람 홈 검색 시 검색할 음악 정보")
    public ApiResponse<Page<MusicDetailsResponse>> getMusicDetailsList(
            @RequestParam String title,
            @PageableDefault(size = 10) Pageable pageable
    ){
        return ApiResponse.ok(musicService.getMusicDetailsList(title, pageable));
    }

}
