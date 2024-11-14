package com.cuk.damda.contents.controller.request;

import com.cuk.damda.contents.domain.Enum.Rating;

public record ReviewRequest(
        String review,
        Rating rating
) {
}

