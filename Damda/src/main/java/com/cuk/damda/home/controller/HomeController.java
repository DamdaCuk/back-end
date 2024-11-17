package com.cuk.damda.home.controller;

import com.cuk.damda.global.controller.ApiResponse;
import com.cuk.damda.home.controller.request.HomeNameRequest;
import com.cuk.damda.home.controller.response.HomeListResponse;
import com.cuk.damda.home.domain.Home;
import com.cuk.damda.home.service.HomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/home")
@Tag(name="홈 컨트롤러")
public class HomeController {

    private final HomeService homeService;

    @PostMapping("/create")
    @Operation(summary = "홈 생성", description = "회원가입 후 홈 생성할 때 사용하는 API")
    public ApiResponse<String> createHome(@RequestBody HomeNameRequest homeNameRequest
            , @AuthenticationPrincipal UserDetails user
    ) {
        Home home = homeService.createHome(homeNameRequest.homeName());
        homeService.memberInsertHome(home, user.getUsername());

        return ApiResponse.of(HttpStatus.CREATED, "success");
    }

    @GetMapping("/search")
    @Operation(summary = "컨텐츠로 다른 사람 홈 검색", description = "검색된 홈 리스트 반환 itemId, 페이지 값 필요")
    public ApiResponse<Page<HomeListResponse>> searchHome(
            @RequestParam Long itemId,
            @RequestParam String contentType,
            @PageableDefault(size = 10) Pageable pageable) {

        Page<HomeListResponse> homeResponses = homeService.searchHomes(itemId, contentType, pageable);
        return ApiResponse.of(HttpStatus.OK, "success", homeResponses);
    }
}
