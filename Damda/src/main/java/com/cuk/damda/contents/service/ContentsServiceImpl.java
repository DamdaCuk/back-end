package com.cuk.damda.contents.service;

import com.cuk.damda.contents.controller.request.ReviewRequest;
import com.cuk.damda.contents.controller.response.ReviewResponse;
import com.cuk.damda.contents.domain.Contents;
import com.cuk.damda.contents.repository.ContentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ContentsServiceImpl implements ContentsService {
    private final ContentsRepository contentsRepository;
    @Override
    public ReviewResponse addAndUpdateReview(Long contentId, ReviewRequest reviewRequest) {
        // 1. `homeId`와 `contentId`에 해당하는 콘텐츠 조회
        Contents content = contentsRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 콘텐츠를 찾을 수 없습니다."));

        // 2. 리뷰가 있는 경우 업데이트, 없는 경우 추가
        if (content.getReview() != null) {
            // 이미 리뷰가 있는 경우 업데이트
            content.review(reviewRequest.review());
            content.rating(reviewRequest.rating());
        } else {
            // 리뷰가 없는 경우 추가
            content.addReview(reviewRequest.review(), reviewRequest.rating());
        }
        // 3. 변경사항 저장
        contentsRepository.save(content);
        return ReviewResponse.from(content.getReview(), content.getRating());
    }

    @Override
    public ReviewResponse searchReview(Long contentId) {
        Contents content = contentsRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 콘텐츠를 찾을 수 없습니다."));
        return ReviewResponse.from(content.getReview(), content.getRating());
    }


    @Override
    public void deleteReview(Long contentId) {
        Contents content = contentsRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 콘텐츠를 찾을 수 없습니다."));

        // 리뷰와 평점 필드 초기화
        content.review(null);
        content.rating(null);

        // 변경된 내용을 저장
        contentsRepository.save(content);
    }


}
