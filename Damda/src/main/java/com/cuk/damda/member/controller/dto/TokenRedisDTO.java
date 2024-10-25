package com.cuk.damda.member.controller.dto;

import com.cuk.damda.member.domain.TokenRedis;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Service;

public record TokenRedisDTO(
        Long userId,
        String refreshToken,
        String accessToken
) {
    public static TokenRedisDTO of(Long userId, String refreshToken, String accessToken) {
        return new TokenRedisDTO(userId, refreshToken, accessToken);
    }

    public static TokenRedisDTO toDTO(TokenRedis tokenRedis) {
        return new TokenRedisDTO(
                tokenRedis.getUserId(), tokenRedis.getRefreshToken(), tokenRedis.getAccessToken()
        );
    }

    public static TokenRedis toEntity(TokenRedisDTO tokenRedisDTO) {
        return new TokenRedis(
                tokenRedisDTO.refreshToken, tokenRedisDTO.userId(), tokenRedisDTO.accessToken()
        );
    }
}
