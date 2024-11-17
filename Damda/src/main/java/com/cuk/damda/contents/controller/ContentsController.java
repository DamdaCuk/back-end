package com.cuk.damda.contents.controller;

import com.cuk.damda.contents.controller.response.ContentsListResponse;
import com.cuk.damda.contents.domain.Enum.ItemType;
import com.cuk.damda.contents.service.ContentsService;
import com.cuk.damda.global.controller.ApiResponse;
import com.cuk.damda.contents.controller.request.ReviewRequest;
import com.cuk.damda.contents.controller.response.ReviewResponse;
import com.cuk.damda.contents.service.ContentsService;
import com.cuk.damda.global.controller.ApiResponse;
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
public class ContentsController {

    private final ContentsService contentsService;

    @GetMapping("/{itemType}/{homeId}/list")
    public ApiResponse<Slice<ContentsListResponse>> getContentsList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @PathVariable Long homeId,
            @PathVariable String itemType){
        ItemType type = ItemType.valueOf(itemType.toUpperCase());
        return ApiResponse.ok(contentsService.getContentsList(page, size, homeId, type));
    }

    @PostMapping("/{contentId}/review")
    public ApiResponse<?> addAndUpdateReview(@PathVariable Long contentId, @RequestBody ReviewRequest reviewRequest, @AuthenticationPrincipal
                                             UserDetails user){
        ReviewResponse reviewResponse = contentsService.addAndUpdateReview(contentId,reviewRequest, user.getUsername());
        return ApiResponse.of(HttpStatus.CREATED,"success", reviewResponse);
    }

    @GetMapping("{contentId}/review")
    public ApiResponse<?> searchReview(@PathVariable Long contentId){
        ReviewResponse reviewResponse = contentsService.searchReview(contentId);
        return ApiResponse.of(HttpStatus.OK,"success",reviewResponse);
    }

    @DeleteMapping("/{contentId}/review")
    public ApiResponse<?> deleteReview(@PathVariable Long contentId, @AuthenticationPrincipal UserDetails user){
        contentsService.deleteReview(contentId, user.getUsername());
        return ApiResponse.of(HttpStatus.OK, "Review and rating deleted successfully");
    }

    @DeleteMapping("/{contentsId}")
    public ApiResponse<Long> deleteContents(@PathVariable Long contentsId, @AuthenticationPrincipal UserDetails user){
        contentsService.deleteContents(contentsId, user.getUsername());
        return ApiResponse.ok(contentsId);
    }
}
