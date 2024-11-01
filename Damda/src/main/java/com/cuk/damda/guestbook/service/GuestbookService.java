package com.cuk.damda.guestbook.service;

import com.cuk.damda.guestbook.controller.request.LikeRequest;

public interface GuestbookService {
    void addLike(LikeRequest likeRequest, String email);
}
