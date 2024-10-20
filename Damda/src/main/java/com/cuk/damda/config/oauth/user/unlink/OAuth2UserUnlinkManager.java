package com.cuk.damda.config.oauth.user.unlink;

import com.cuk.damda.config.oauth.exception.OAuth2AuthenticationProcessingException;
import com.cuk.damda.config.oauth.user.OAuth2Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OAuth2UserUnlinkManager {

    private final NaverUserUnlink naverUserUnlink;

    public void unlink(OAuth2Provider provider, String accessToken){
        if(OAuth2Provider.NAVER.equals(provider)){
            naverUserUnlink.unlink(accessToken);
        }else{
            throw new OAuth2AuthenticationProcessingException("Unlink with "+provider.getRegistrationId()+" is not supported");
        }
    }
}
