package com.cuk.damda.contents.service;

import com.cuk.damda.contents.controller.request.ReviewRequest;
import com.cuk.damda.contents.controller.response.ReviewResponse;

public interface ContentsService {

    ReviewResponse addAndUpdateReview(Long contentId, ReviewRequest reviewRequest);
    ReviewResponse searchReview(Long contentId);

    void deleteReview(Long contentId);
}
