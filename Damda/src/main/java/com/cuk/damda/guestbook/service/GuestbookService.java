package com.cuk.damda.guestbook.service;

import com.cuk.damda.guestbook.controller.request.CommentRequest;
import com.cuk.damda.guestbook.controller.request.GetCommentRequest;
import com.cuk.damda.guestbook.controller.response.GetCommentsResponse;
import java.util.List;

public interface GuestbookService {

    void addComment(CommentRequest commentRequest, String username);

    List<GetCommentsResponse> getComments(GetCommentRequest getCommentRequest);
}
