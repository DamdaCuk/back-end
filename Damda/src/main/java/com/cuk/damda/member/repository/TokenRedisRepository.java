package com.cuk.damda.member.repository;

import com.cuk.damda.member.domain.TokenRedis;
import org.springframework.data.repository.CrudRepository;

public interface TokenRedisRepository extends CrudRepository<TokenRedis, Long> {
    TokenRedis findByAccessToken(String accessToken);
}
