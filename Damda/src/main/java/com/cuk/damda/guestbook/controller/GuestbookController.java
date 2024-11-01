package com.cuk.damda.guestbook.controller;

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

    @PostMapping
    public void addLike(@RequestBody LikeRequest likeRequest, @AuthenticationPrincipal UserDetails userDetails) {
        String email=userDetails.getUsername();
        guestbookService.addLike(likeRequest, email);
    }
}
