package com.cuk.damda.config.oauth.user.unlink;

import com.cuk.damda.config.oauth.exception.OAuth2AuthenticationProcessingException;
import com.cuk.damda.config.oauth.user.OAuth2Provider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OAuth2UserUnlinkManager {

    private final NaverUserUnlink naverUserUnlink;
    private final HttpServletResponse httpServletResponse;

    public void unlink(OAuth2Provider provider, String accessToken, String userEmail, HttpServletRequest request, HttpServletResponse response) {
        if(OAuth2Provider.NAVER.equals(provider)){
            naverUserUnlink.unlink(accessToken, userEmail, request, response);
        }else{
            throw new OAuth2AuthenticationProcessingException("Unlink with "+provider.getRegistrationId()+" is not supported");
        }
    }
}
