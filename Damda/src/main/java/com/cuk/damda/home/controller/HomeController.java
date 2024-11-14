package com.cuk.damda.home.controller;

import com.cuk.damda.global.controller.ApiResponse;
import com.cuk.damda.home.controller.request.HomeNameRequest;
import com.cuk.damda.home.controller.response.HomeResponse;
import com.cuk.damda.home.domain.Home;
import com.cuk.damda.home.service.HomeService;
import lombok.RequiredArgsConstructor;
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
    public ApiResponse<?> createHome(@RequestBody HomeNameRequest homeNameRequest
            , @AuthenticationPrincipal UserDetails user
    ) {
        Home home = homeService.createHome(homeNameRequest.homeName());
        homeService.memberInsertHome(home, user.getUsername());

        return ApiResponse.of(HttpStatus.CREATED, "success");
    }

    @GetMapping("/search")
    public ApiResponse<?> searchHome(@RequestParam String title, @RequestParam String contentType){
        List<HomeResponse> homeDetailsList = homeService.searchHome(title, contentType);
        return ApiResponse.of(HttpStatus.OK, "success", homeDetailsList);
    }
}
