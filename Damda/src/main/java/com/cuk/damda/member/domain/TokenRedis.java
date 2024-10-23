package com.cuk.damda.member.domain;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@ToString
@RedisHash(value = "token", timeToLive = 60 * 60 * 24 * 14) //리프레시 토큰의 expire time
public class TokenRedis {

    @Id
    private Long userId;
    private String refreshToken;
    @Indexed
    private String accessToken;

    public TokenRedis(String refreshToken, Long userId, String accessToken) {
        this.userId=userId;
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }

    public void updateAccessToken(String accessToken){
        this.accessToken=accessToken;
    }
}
