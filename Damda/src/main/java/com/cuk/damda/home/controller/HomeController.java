package com.cuk.damda.home.controller;

import com.cuk.damda.global.controller.ApiResponse;
import com.cuk.damda.home.controller.request.HomeNameRequest;
import com.cuk.damda.home.controller.response.HomeListResponse;
import com.cuk.damda.home.domain.Home;
import com.cuk.damda.home.service.HomeService;
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
public class HomeController {

    private final HomeService homeService;

    @PostMapping("/create")
    public ApiResponse<String> createHome(@RequestBody HomeNameRequest homeNameRequest
            , @AuthenticationPrincipal UserDetails user
    ) {
        Home home = homeService.createHome(homeNameRequest.homeName());
        homeService.memberInsertHome(home, user.getUsername());

        return ApiResponse.of(HttpStatus.CREATED, "success");
    }

    @GetMapping("/search")
    public ApiResponse<Page<HomeListResponse>> searchHome(
            @RequestParam Long itemId,
            @RequestParam String contentType,
            @PageableDefault(size = 10) Pageable pageable) {

        Page<HomeListResponse> homeResponses = homeService.searchHomes(itemId, contentType, pageable);
        return ApiResponse.of(HttpStatus.OK, "success", homeResponses);
    }
}
