package com.cuk.damda.guestbook.controller;

import com.cuk.damda.global.controller.ApiResponse;
import com.cuk.damda.guestbook.controller.request.CommentRequest;
import com.cuk.damda.guestbook.controller.request.DeleteCommentRequest;
import com.cuk.damda.guestbook.controller.request.UpdateCommentRequest;
import com.cuk.damda.guestbook.controller.response.GetCommentsResponse;
import com.cuk.damda.guestbook.service.GuestbookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name="방명록 컨트롤러")
public class GuestbookController {
    private final GuestbookService guestbookService;

    @PostMapping("/comment")
    @Operation(summary = "한줄평 작성", description = "한줄평과 homeId를 주면 로그인 한 유저가 해당 Home에 한줄평을 작성 함")
    public ApiResponse<?> addComment(@RequestBody CommentRequest commentRequest
            , @AuthenticationPrincipal UserDetails userDetails) {
        guestbookService.addComment(commentRequest, userDetails.getUsername());
        return ApiResponse.ok("success");
    }

    @GetMapping("/comment/{homeId}")
    @Operation(summary = "한줄평 불러오기", description = "home에 저장된 한줄평들을 불러옵니다.")
    public ApiResponse<?> getComments(@PathVariable Long homeId) {
        List<GetCommentsResponse> response=guestbookService.getComments(homeId);
        return ApiResponse.ok(response);
    }

    @PutMapping("/comment")
    @Operation(summary = "한줄평 수정", description = "코멘트 id와 수정할 코멘트를 주면 로그인 한 유저가 해당 코멘트를 작성했는지 여부 확인 후 수정")
    public ApiResponse<?>updateComment(@RequestBody UpdateCommentRequest updateCommentRequest
            , @AuthenticationPrincipal UserDetails userDetails) {
        guestbookService.updateComment(updateCommentRequest, userDetails.getUsername());
        return ApiResponse.ok(updateCommentRequest.comment());
    }

    @DeleteMapping("/comment")
    @Operation(summary = "한줄평 삭제", description = "삭제 할 코멘트 id를 주면 로그인 한 유저가 해당 코멘트를 작성했는지 여부 확인 후 삭제")
    public ApiResponse<?>deleteComment(@RequestBody DeleteCommentRequest deleteCommentRequest
            , @AuthenticationPrincipal UserDetails userDetails){
        guestbookService.deleteComment(deleteCommentRequest, userDetails.getUsername());
        return ApiResponse.ok("success");
    }

    @PostMapping("/like/add")
    @Operation(summary = "좋아요 달기", description = "homeId를 주면 로그인 한 유저가 해당 홈에 좋아요를 추가 합니다")
    public ApiResponse<String> addLike(@RequestBody LikeRequest likeRequest
            , @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email=userDetails.getUsername();
        guestbookService.addLike(likeRequest, email);
        return ApiResponse.ok("좋아요를 성공적으로 추가함");
    }

    //로그인 한 유저가 해당 홈에 좋아요를 눌렀는지 확인
    @PostMapping("/like")
    @Operation(summary = "좋아요 확인", description = "homeId를 주면 로그인 한 유저가 해당 홈에 좋아요를 눌렀는지 확인한 후 결과를 전송합니다.")
    public ApiResponse<Boolean> isLike(@RequestBody LikeRequest likeRequest
            , @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email=userDetails.getUsername();
        return ApiResponse.ok(guestbookService.isLike(likeRequest,email));
    }

    @PostMapping("/like/delete")
    @Operation(summary = "좋아요 삭제", description = "homeId를 주면 로그인 한 유저가 해당 홈에 좋아요를 눌렀는지 확인 후 좋아요가 눌러져 있으면 삭제합니다.")
    public ApiResponse<String> deleteLike(@RequestBody LikeRequest likeRequest
            , @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email=userDetails.getUsername();
        guestbookService.deleteLike(likeRequest, email);
        return ApiResponse.ok("성공적으로 좋아요를 취소함");
    }
}
