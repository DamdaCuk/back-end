package com.cuk.damda.config.oauth.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OAuth2Provider {
    NAVER("naver")

    ;

    private final String registrationId;
}
