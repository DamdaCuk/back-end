package com.cuk.damda.member.service;

import com.cuk.damda.config.jwt.TokenProvider;
import com.cuk.damda.config.oauth.exception.TokenException;
import com.cuk.damda.member.domain.TokenRedis;
import com.cuk.damda.member.repository.TokenRedisRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final MemberService memberService;
    private final TokenRedisRepository tokenRedisRepository;

    @Value("${jwt.secretKey}")
    private String secret;
    private Key key;

    @PostConstruct
    public void init(){
        byte[] key= Decoders.BASE64.decode(secret);
        this.key= Keys.hmacShaKeyFor(key);
    }

    public String validateAndRefreshToken(String accessToken) {

        //액세스 토큰 검증
        if(tokenProvider.validateToken(accessToken)) {
            return accessToken; //액세스 토큰이 유효하면 그대로 반환
        }

        // 액세스 토큰이 만료되었을 경우
        // Redis에서 만료된 액세스 토큰으로 리프레시 토큰 찾기
        TokenRedis tokenRedis = tokenRedisRepository.findByAccessToken(accessToken);
        String refreshToken=tokenRedis.getRefreshToken();

        // 리프레시 토큰 검증
        if (tokenProvider.validateToken(refreshToken)) {
            // 리프레시 토큰이 유효하면 새로운 액세스 토큰 발급
            Long memberId=tokenRedis.getUserId();
            Authentication authentication = tokenProvider.getAuthentication(refreshToken);
            String newAccessToken= tokenProvider.makeToken(authentication, memberId);

            //액세스토큰 업데이트
            tokenRedis.updateAccessToken(newAccessToken);
            tokenRedisRepository.save(tokenRedis);
            return newAccessToken;
        }

        // 리프레시 토큰도 유효하지 않으면 예외 처리
        throw new TokenException("Invalid refresh token");
    }

}
