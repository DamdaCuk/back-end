package com.cuk.damda.contents.service;

import com.cuk.damda.contents.controller.response.ContentsListResponse;
import com.cuk.damda.contents.domain.Contents;
import com.cuk.damda.contents.domain.Enum.ItemType;
import com.cuk.damda.contents.repository.ContentsRepository;
import com.cuk.damda.home.domain.Home;
import com.cuk.damda.home.repository.HomeRepository;
import com.cuk.damda.contents.controller.request.ReviewRequest;
import com.cuk.damda.contents.controller.response.ReviewResponse;
import com.cuk.damda.contents.domain.Contents;
import com.cuk.damda.contents.repository.ContentsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class ContentsServiceImpl implements ContentsService {
    private final HomeRepository homeRepository;
    private final ContentsRepository contentsRepository;

    @Override
    public Slice<ContentsListResponse> getContentsList(int page, int size, Long homeId, ItemType itemType) {
        log.info("ItemType: " + itemType);

        Home testHome = homeRepository.findByHomeId(homeId)
                .orElseThrow(() -> new IllegalArgumentException("home을 찾을 수 없습니다."));

        Pageable pageable = PageRequest.of(page, size);
        Slice<Contents> contentsList = contentsRepository.findByHomeAndItemType(pageable, testHome, itemType);

        Slice<ContentsListResponse> contentsListResponses = contentsList.map(contents ->
                    ContentsListResponse.from(
                        contents.getItemId(),
                        contents.getItemTitle(),
                        contents.getItemImg()
                    )
                );

        return contentsListResponses;
    }
    @Override
    public ReviewResponse addAndUpdateReview(Long contentId, ReviewRequest reviewRequest) {
        // 1. `contentId`에 해당하는 콘텐츠 조회
        Contents content = contentsRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 콘텐츠를 찾을 수 없습니다."));

        // 2. 리뷰나 평점 중 하나라도 존재하는 경우 업데이트, 둘 다 없으면 추가
        if (content.getReview() != null || content.getRating() != null) {
            // 이미 리뷰나 평점이 있는 경우 개별적으로 업데이트
            if (reviewRequest.review() != null) {
                content.review(reviewRequest.review()); // 리뷰 업데이트
            }
            if (reviewRequest.rating() != null) {
                content.rating(reviewRequest.rating()); // 평점 업데이트
            }
        } else {
            // 리뷰와 평점이 모두 없는 경우 추가
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
