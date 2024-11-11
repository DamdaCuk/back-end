package com.cuk.damda.guestbook.controller;

import com.cuk.damda.global.controller.ApiResponse;
import com.cuk.damda.guestbook.controller.request.LikeRequest;
import com.cuk.damda.guestbook.service.GuestbookService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/guest-book")
public class GuestbookController {
    private final GuestbookService guestbookService;

    @PostMapping("/like/add")
    public void addLike(@RequestBody LikeRequest likeRequest, @AuthenticationPrincipal UserDetails userDetails) {
        String email=userDetails.getUsername();
        guestbookService.addLike(likeRequest, email);
    }

    //로그인 한 유저가 해당 홈에 좋아요를 눌렀는지 확인
    @PostMapping("/like")
    public ApiResponse<?> isLike(@RequestBody LikeRequest likeRequest, @AuthenticationPrincipal UserDetails userDetails) {
        String email=userDetails.getUsername();
        return ApiResponse.ok(guestbookService.isLike(likeRequest,email));
    }

    @PostMapping("/like/delete")
    public ApiResponse<?> deleteLike(@RequestBody LikeRequest likeRequest, @AuthenticationPrincipal UserDetails userDetails) {
        String email=userDetails.getUsername();
        guestbookService.deleteLike(likeRequest, email);
        return ApiResponse.ok("성공적으로 좋아요를 취소함");
    }
}
