package com.cuk.damda.config.oauth.user;

import java.util.Map;

public interface OAuth2UserInfo {
    OAuth2Provider getProvider();
    String getAccessToken();
    Map<String, Object> getAttributes();
    String getId();
    String getEmail();
    String getName();

}
