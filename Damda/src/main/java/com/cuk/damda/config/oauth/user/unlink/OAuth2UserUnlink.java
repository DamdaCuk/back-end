package com.cuk.damda.config.oauth.user.unlink;

public interface OAuth2UserUnlink {
    void unlink(String accessToken, String userEmail);
}
