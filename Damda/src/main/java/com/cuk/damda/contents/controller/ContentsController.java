package com.cuk.damda.contents.controller;

import com.cuk.damda.contents.controller.request.ReviewRequest;
import com.cuk.damda.contents.controller.response.ReviewResponse;
import com.cuk.damda.contents.service.ContentsService;
import com.cuk.damda.global.controller.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/{homeId}/contents")
public class ContentsController {
    private final ContentsService contentsService;

    @PostMapping("/{contentId}/review")
    public ApiResponse<?> addReview(@PathVariable Long homeId, @PathVariable Long contentId, @RequestBody ReviewRequest reviewRequest){
        contentsService.addReview(homeId,contentId,reviewRequest);
        return ApiResponse.of(HttpStatus.CREATED,"success");
    }

    @GetMapping("{contentId}/review")
    public ApiResponse<?> searchReview(@PathVariable Long homeId,@PathVariable Long contentId){
        ReviewResponse reviewResponse = contentsService.searchReview(homeId, contentId);
        return ApiResponse.of(HttpStatus.OK,"success",reviewResponse);
    }

    @PatchMapping("/{contentId}/review")
    public ApiResponse<ReviewResponse> updateReviewAndRating(@PathVariable Long homeId,@PathVariable Long contentId, @RequestBody ReviewRequest updateRequest) {
        ReviewResponse updatedReview = contentsService.updateReview(homeId, contentId, updateRequest);
        return ApiResponse.of(HttpStatus.OK, "Review and rating updated successfully", updatedReview);
    }

    @DeleteMapping("/{contentId}/review")
    public ApiResponse<?> deleteReview(@PathVariable Long homeId,@PathVariable Long contentId){
        contentsService.deleteReview(homeId, contentId);
        return ApiResponse.of(HttpStatus.OK, "Review and rating deleted successfully");
    }
}
