package com.cuk.damda.contents.controller.response;

import com.cuk.damda.contents.domain.Enum.Rating;

public record ReviewResponse(
        String reviewText,
        Rating rating
) {
    // from 메서드: 주어진 값들을 기반으로 ReviewResponse 인스턴스를 생성
    public static ReviewResponse from(String reviewText, Rating rating) {
        return new ReviewResponse(
                reviewText,
                rating
        );
    }
}