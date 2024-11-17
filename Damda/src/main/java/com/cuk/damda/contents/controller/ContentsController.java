package com.cuk.damda.contents.controller;

import com.cuk.damda.contents.controller.response.ContentsListResponse;
import com.cuk.damda.contents.domain.Enum.ItemType;
import com.cuk.damda.contents.service.ContentsService;
import com.cuk.damda.global.controller.ApiResponse;
import com.cuk.damda.contents.controller.request.ReviewRequest;
import com.cuk.damda.contents.controller.response.ReviewResponse;
import com.cuk.damda.contents.service.ContentsService;
import com.cuk.damda.global.controller.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/contents")
@Tag(name = "컨텐츠 컨트롤러")
public class ContentsController {

    private final ContentsService contentsService;

    @Operation(summary = "타입 별 컨텐츠 조회",description = "아이템 타입 별 컨텐츠 스크롤로 구현")
    @GetMapping("/{itemType}/{homeId}/list")
    public ApiResponse<Slice<ContentsListResponse>> getContentsList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @PathVariable Long homeId,
            @PathVariable String itemType){
        ItemType type = ItemType.valueOf(itemType.toUpperCase());
        return ApiResponse.ok(contentsService.getContentsList(page, size, homeId, type));
    }

    @Operation(summary = "컨텐츠에 리뷰와 평점 달기",description = "리뷰와 평점을 보내면 db에 저장됨. 리뷰나 평점만 보낼 수 있음")
    @PostMapping("/{contentId}/review")
    public ApiResponse<?> addAndUpdateReview(@PathVariable Long contentId, @RequestBody ReviewRequest reviewRequest, @AuthenticationPrincipal
                                             UserDetails user){
        ReviewResponse reviewResponse = contentsService.addAndUpdateReview(contentId,reviewRequest, user.getUsername());
        return ApiResponse.of(HttpStatus.CREATED,"success", reviewResponse);
    }

    @Operation(summary = "컨텐츠에 리뷰와 평점 조회",description = "컨텐츠 id를 통해 해당 컨텐츠의 리뷰와 평점을 조회할 수 있음")
    @GetMapping("{contentId}/review")
    public ApiResponse<?> searchReview(@PathVariable Long contentId){
        ReviewResponse reviewResponse = contentsService.searchReview(contentId);
        return ApiResponse.of(HttpStatus.OK,"success",reviewResponse);
    }

    @Operation(summary = "컨텐츠에 리뷰와 평점 삭제",description = "리뷰와 평점 둘다 삭제하는 기능")
    @DeleteMapping("/{contentId}/review")
    public ApiResponse<?> deleteReview(@PathVariable Long contentId, @AuthenticationPrincipal UserDetails user){
        contentsService.deleteReview(contentId, user.getUsername());
        return ApiResponse.of(HttpStatus.OK, "Review and rating deleted successfully");
    }

    @Operation(summary = "컨텐츠 삭제",description = "컨텐츠 아이디로 컨텐츠 삭제")
    @DeleteMapping("/{contentsId}")
    public ApiResponse<Long> deleteContents(@PathVariable Long contentsId, @AuthenticationPrincipal UserDetails user){
        contentsService.deleteContents(contentsId, user.getUsername());
        return ApiResponse.ok(contentsId);
    }
}
