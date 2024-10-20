package com.cuk.damda.config.oauth.user.unlink;

import com.cuk.damda.config.oauth.exception.OAuth2AuthenticationProcessingException;
import com.cuk.damda.member.domain.Member;
import com.cuk.damda.member.repository.MemberRepository;
import com.cuk.damda.member.repository.RefreshTokenRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
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
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    @Override
    public void unlink(String accessToken, String userEmail) {
        String url = URL +
                "?grant_type=delete" +
                "&client_id=" +
                clientId +
                "&client_secret=" +
                clientSecret +
                "&access_token=" +
                accessToken;
        UnlinkResponse response=restTemplate.getForObject(url, UnlinkResponse.class);

        if(response!=null && !"success".equalsIgnoreCase(response.getResult())){
            throw new RuntimeException("Failed to Naver Unlink");
        }
        Optional<Member> member = memberRepository.findByEmail(userEmail);
        if(member.isEmpty()){
            throw new OAuth2AuthenticationProcessingException("사용자 정보를 찾을 수 없음");
        }
        refreshTokenRepository.deleteByMemberId(member.get().getUserId());
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
