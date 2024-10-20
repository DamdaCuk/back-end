package com.cuk.damda.config.oauth;

import com.cuk.damda.config.oauth.exception.OAuth2AuthenticationProcessingException;
import com.cuk.damda.config.oauth.user.OAuth2UserInfo;
import com.cuk.damda.config.oauth.user.OAuth2UserInfoFactory;
import com.cuk.damda.member.domain.Member;
import com.cuk.damda.member.repository.MemberRepository;
import io.netty.util.internal.StringUtil;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Service
public class OAuth2UserCustomService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        //상위 클래스에 정의된 액세스 토큰으로 사용자 정보를 가져오는 로직 실행
        OAuth2User user=super.loadUser(userRequest);

        try{
            return processOAuth2User(userRequest, user);
        }catch(AuthenticationException e){
            throw e;
        }catch(Exception e){
            throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User user){
        //각 OAuth2 제공자 별 제공되는 사용자 정보를 동일한 인터페이스로 변환하여 리턴
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String accessToken = userRequest.getAccessToken().getTokenValue();

        OAuth2UserInfo oAuth2UserInfo= OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, accessToken, user.getAttributes());

        //OAuth2UserInfo field value validataion
        if(!StringUtils.hasText(oAuth2UserInfo.getEmail())){
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }
        return new OAuth2UserPrincipal(oAuth2UserInfo);
    }
}