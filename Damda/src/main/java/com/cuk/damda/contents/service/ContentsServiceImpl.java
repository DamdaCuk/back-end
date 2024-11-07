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
    public void addReview(Long homeId, Long contentId, ReviewRequest reviewRequest) {
        // 1. `homeId`와 `contentId`에 해당하는 콘텐츠 조회
        Contents content = contentsRepository.findByHome_HomeIdAndContentsId(homeId, contentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 콘텐츠를 찾을 수 없습니다."));

        // 2. 리뷰 및 평점 추가
        content.addReview(reviewRequest.review(), reviewRequest.rating());

        // 3. 변경사항 저장
        contentsRepository.save(content);
    }

    @Override
    public ReviewResponse searchReview(Long homeId, Long contentId) {
        Contents content = contentsRepository.findByHome_HomeIdAndContentsId(homeId, contentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 콘텐츠를 찾을 수 없습니다."));
        return ReviewResponse.from(content.getReview(), content.getRating());
    }

    public ReviewResponse updateReview(Long homeId, Long contentId,ReviewRequest updateRequest) {
        Contents content = contentsRepository.findByHome_HomeIdAndContentsId(homeId, contentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 콘텐츠를 찾을 수 없습니다."));

        // 요청 필드가 null이 아닌 경우에만 업데이트
        if (updateRequest.review() != null) {
            content.setReview(updateRequest.review());
        }
        if (updateRequest.rating() != null) {
            content.setRating(updateRequest.rating());
        }

        // 변경된 내용 저장
        Contents updatedContent = contentsRepository.save(content);

        // 저장된 엔티티를 ReviewResponse로 변환하여 반환
        return ReviewResponse.from(updatedContent.getReview(), updatedContent.getRating());
    }

    @Override
    public void deleteReview(Long homeId, Long contentId) {
        Contents content = contentsRepository.findByHome_HomeIdAndContentsId(homeId, contentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 콘텐츠를 찾을 수 없습니다."));

        // 리뷰와 평점 필드 초기화
        content.setReview(null);
        content.setRating(null);

        // 변경된 내용을 저장
        contentsRepository.save(content);
    }


}
