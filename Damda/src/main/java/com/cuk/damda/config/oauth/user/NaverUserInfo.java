package com.cuk.damda.config.oauth.user;

import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;
    private final String accessToken;
    private final String id;
    private final String email;
    private final String name;

    public NaverUserInfo(Map<String, Object> attributes, String accessToken) {
        this.accessToken=accessToken;
        this.attributes=(Map<String, Object>) attributes.get("response");
        this.id=(String)this.attributes.get("id");
        this.email=(String)this.attributes.get("email");
        this.name=(String)this.attributes.get("name");
    }


    @Override
    public OAuth2Provider getProvider() {
        return OAuth2Provider.NAVER;
    }

    @Override
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getName() {
        return name;
    }
}
