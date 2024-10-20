package com.cuk.damda.member.service;

import com.cuk.damda.member.domain.Member;
import com.cuk.damda.member.domain.RefreshToken;
import com.cuk.damda.member.repository.RefreshTokenRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken createRefreshToken(Member member, String token, LocalDateTime expireTime) {
        RefreshToken refreshToken=RefreshToken.create(token, expireTime, member);
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public void deleteRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.delete(refreshToken);
    }
}
