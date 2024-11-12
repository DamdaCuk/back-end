package com.cuk.damda.guestbook.service;

import com.cuk.damda.guestbook.controller.request.CommentRequest;

public interface GuestbookService {

    void addComment(CommentRequest commentRequest, String username);
}
