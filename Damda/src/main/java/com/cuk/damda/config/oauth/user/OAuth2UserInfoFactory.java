package com.cuk.damda.config.oauth.user;

import java.util.Map;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, String accessToken, Map<String, Object> attributes) {
        if(OAuth2Provider.NAVER.getRegistrationId().equals(registrationId)) {
            return new NaverUserInfo(attributes, accessToken);
        }else{
            throw new OAuth2AuthenticationException("Login with "+registrationId+" is not supported");
        }
    }
}
