package com.cuk.damda.contents.service;

import com.cuk.damda.contents.controller.request.ReviewRequest;
import com.cuk.damda.contents.controller.response.ReviewResponse;

public interface ContentsService {

    void addReview(Long homeId, Long contentId, ReviewRequest reviewRequest);
    ReviewResponse searchReview(Long homeId, Long contentId);
    ReviewResponse updateReview(Long homeId, Long contentId, ReviewRequest reviewRequest);

    void deleteReview(Long homeId, Long contentId);
}
