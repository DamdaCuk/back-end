package com.cuk.damda.guestbook.controller;

import com.cuk.damda.global.controller.ApiResponse;
import com.cuk.damda.guestbook.controller.request.CommentRequest;
import com.cuk.damda.guestbook.controller.request.DeleteCommentRequest;
import com.cuk.damda.guestbook.controller.request.GetCommentRequest;
import com.cuk.damda.guestbook.controller.request.UpdateCommentRequest;
import com.cuk.damda.guestbook.controller.response.GetCommentsResponse;
import com.cuk.damda.guestbook.service.GuestbookService;
import com.cuk.damda.member.domain.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping("/comment")
    public ApiResponse<?> getComments(@RequestBody GetCommentRequest getCommentRequest) {
        List<GetCommentsResponse> response=guestbookService.getComments(getCommentRequest);
        return ApiResponse.ok(response);
    }

    @PutMapping("/comment")
    public ApiResponse<?>updateComment(@RequestBody UpdateCommentRequest updateCommentRequest
            , @AuthenticationPrincipal UserDetails userDetails) {
        guestbookService.updateComment(updateCommentRequest, userDetails.getUsername());
        return ApiResponse.ok(updateCommentRequest.comment());
    }

    @DeleteMapping("/comment")
    public ApiResponse<?>deleteComment(@RequestBody DeleteCommentRequest deleteCommentRequest
            , @AuthenticationPrincipal UserDetails userDetails){
        guestbookService.deleteComment(deleteCommentRequest, userDetails.getUsername());
        return ApiResponse.ok("success");
    }
}
