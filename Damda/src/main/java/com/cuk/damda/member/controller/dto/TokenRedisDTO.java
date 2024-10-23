package com.cuk.damda.member.controller.dto;

import com.cuk.damda.member.domain.TokenRedis;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Service;

@Getter
@Service
@ToString
public class TokenRedisDTO {
    private Long userId;
    private String refreshToken;
    private String accessToken;

    public TokenRedisDTO(Long userId, String refreshToken, String accessToken) {
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }

    public TokenRedisDTO() {}

    public static TokenRedisDTO toDTO(TokenRedis tokenRedis){
        TokenRedisDTO dto=new TokenRedisDTO();
        dto.refreshToken=tokenRedis.getRefreshToken();
        dto.userId=tokenRedis.getUserId();
        dto.accessToken=tokenRedis.getAccessToken();
        return dto;
    }

    public static TokenRedis toEntity(TokenRedisDTO tokenRedisDTO){
        TokenRedis tokenRedis=new TokenRedis(tokenRedisDTO.getRefreshToken(),tokenRedisDTO.getUserId(), tokenRedisDTO.getAccessToken());
        return tokenRedis;
    }
}
