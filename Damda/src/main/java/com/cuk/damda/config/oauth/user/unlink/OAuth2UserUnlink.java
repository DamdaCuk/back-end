package com.cuk.damda.config.oauth.user.unlink;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface OAuth2UserUnlink {
    void unlink(String accessToken, String userEmail, HttpServletRequest request, HttpServletResponse response);
}
