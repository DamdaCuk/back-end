package com.cuk.damda.config.oauth.user.unlink;

import com.cuk.damda.config.oauth.exception.OAuth2AuthenticationProcessingException;
import com.cuk.damda.member.domain.Member;
import com.cuk.damda.member.repository.MemberRepository;
import com.cuk.damda.member.service.RefreshTokenService;
import com.cuk.damda.util.CookieUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class NaverUserUnlink implements OAuth2UserUnlink {

    private static final String URL="https://nid.naver.com/oauth2.0/token";

    private final RestTemplate restTemplate;
    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;
    private final MemberRepository memberRepository;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    @Override
    public void unlink(String accessToken, String userEmail, HttpServletRequest request, HttpServletResponse response) {
        String url = URL +
                "?grant_type=delete" +
                "&client_id=" +
                clientId +
                "&client_secret=" +
                clientSecret +
                "&access_token=" +
                accessToken;
        UnlinkResponse unlinkResponse=restTemplate.getForObject(url, UnlinkResponse.class);

        if(unlinkResponse!=null && !"success".equalsIgnoreCase(unlinkResponse.getResult())){
            throw new OAuth2AuthenticationProcessingException("Failed to Naver Unlink");
        }
        Optional<Member> member = memberRepository.findByEmail(userEmail);
        if(member.isEmpty()){
            throw new OAuth2AuthenticationProcessingException("Failed to Naver Unlink");
        }
        //리프레시 토큰 redis에서 삭제
        refreshTokenService.deleteRefreshToken(userEmail);

        //토큰 삭제
        CookieUtil.deleteCookie(request,response, "access_token");

        memberRepository.deleteById(member.get().getUserId());
    }

    @Getter
    @RequiredArgsConstructor
    public static class UnlinkResponse{
        @JsonProperty("access_token")
        private final String accessToken;
        private final String result;
    }
}
