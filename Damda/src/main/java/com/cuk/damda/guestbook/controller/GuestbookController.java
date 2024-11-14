package com.cuk.damda.guestbook.controller;

import com.cuk.damda.global.controller.ApiResponse;
import com.cuk.damda.guestbook.controller.request.CommentRequest;
import com.cuk.damda.guestbook.controller.request.DeleteCommentRequest;
import com.cuk.damda.guestbook.controller.request.UpdateCommentRequest;
import com.cuk.damda.guestbook.controller.response.GetCommentsResponse;
import com.cuk.damda.guestbook.service.GuestbookService;
import java.util.List;
import com.cuk.damda.guestbook.controller.request.LikeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/guest-book")
public class GuestbookController {
    private final GuestbookService guestbookService;

    @PostMapping("/comment")
    public ApiResponse<?> addComment(@RequestBody CommentRequest commentRequest
            , @AuthenticationPrincipal UserDetails userDetails) {
        guestbookService.addComment(commentRequest, userDetails.getUsername());
        return ApiResponse.ok("success");
    }

    @GetMapping("/comment/{homeId}")
    public ApiResponse<?> getComments(@PathVariable Long homeId) {
        List<GetCommentsResponse> response=guestbookService.getComments(homeId);
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

    @PostMapping("/like/add")
    public ApiResponse<String> addLike(@RequestBody LikeRequest likeRequest
            , @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email=userDetails.getUsername();
        guestbookService.addLike(likeRequest, email);
        return ApiResponse.ok("좋아요를 성공적으로 추가함");
    }

    //로그인 한 유저가 해당 홈에 좋아요를 눌렀는지 확인
    @PostMapping("/like")
    public ApiResponse<Boolean> isLike(@RequestBody LikeRequest likeRequest
            , @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email=userDetails.getUsername();
        return ApiResponse.ok(guestbookService.isLike(likeRequest,email));
    }

    @PostMapping("/like/delete")
    public ApiResponse<String> deleteLike(@RequestBody LikeRequest likeRequest
            , @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email=userDetails.getUsername();
        guestbookService.deleteLike(likeRequest, email);
        return ApiResponse.ok("성공적으로 좋아요를 취소함");
    }
}
