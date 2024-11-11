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
@RequestMapping()
public class ContentsController {
    private final ContentsService contentsService;

    @PostMapping("/{contentId}/review")
    public ApiResponse<?> addAndUpdateReview(@PathVariable Long contentId, @RequestBody ReviewRequest reviewRequest){
        ReviewResponse reviewResponse = contentsService.addAndUpdateReview(contentId,reviewRequest);
        return ApiResponse.of(HttpStatus.CREATED,"success", reviewResponse);
    }

    @GetMapping("{contentId}/review")
    public ApiResponse<?> searchReview(@PathVariable Long contentId){
        ReviewResponse reviewResponse = contentsService.searchReview(contentId);
        return ApiResponse.of(HttpStatus.OK,"success",reviewResponse);
    }

    @DeleteMapping("/{contentId}/review")
    public ApiResponse<?> deleteReview(@PathVariable Long contentId){
        contentsService.deleteReview(contentId);
        return ApiResponse.of(HttpStatus.OK, "Review and rating deleted successfully");
    }
}
