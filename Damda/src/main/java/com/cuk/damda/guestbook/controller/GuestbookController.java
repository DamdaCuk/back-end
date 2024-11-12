package com.cuk.damda.guestbook.controller;

import com.cuk.damda.global.controller.ApiResponse;
import com.cuk.damda.guestbook.controller.request.CommentRequest;
import com.cuk.damda.guestbook.service.GuestbookService;
import com.cuk.damda.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping
public class GuestbookController {
    private final GuestbookService guestbookService;

    @PostMapping("/comment")
    public ApiResponse<?> addComment(@RequestBody CommentRequest commentRequest
            , @AuthenticationPrincipal UserDetails userDetails) {
        guestbookService.addComment(commentRequest, userDetails.getUsername());
        return ApiResponse.ok("success");
    }
}
