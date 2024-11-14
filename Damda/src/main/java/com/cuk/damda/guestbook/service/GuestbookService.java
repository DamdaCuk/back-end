package com.cuk.damda.guestbook.service;

import com.cuk.damda.guestbook.controller.request.CommentRequest;
import com.cuk.damda.guestbook.controller.request.DeleteCommentRequest;
import com.cuk.damda.guestbook.controller.request.UpdateCommentRequest;
import com.cuk.damda.guestbook.controller.response.GetCommentsResponse;
import java.util.List;

import com.cuk.damda.guestbook.controller.request.LikeRequest;

public interface GuestbookService {
    void addLike(LikeRequest likeRequest, String email);

    boolean isLike(LikeRequest likeRequest, String email);

    void deleteLike(LikeRequest likeRequest, String email);

    void addComment(CommentRequest commentRequest, String username);

    List<GetCommentsResponse> getComments(Long homeId);

    void updateComment(UpdateCommentRequest updateCommentRequest, String userEmail);

    void deleteComment(DeleteCommentRequest deleteCommentRequest, String username);
}
