package com.cuk.damda.member.service;

import com.cuk.damda.member.controller.dto.MemberDTO;
import com.cuk.damda.member.controller.dto.TokenRedisDTO;
import com.cuk.damda.member.domain.Member;
import com.cuk.damda.member.domain.TokenRedis;
import com.cuk.damda.member.repository.MemberRepository;
import com.cuk.damda.member.repository.TokenRedisRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final TokenRedisRepository tokenRedisRepository;
    private final MemberRepository memberRepository;

    //리프레시 토큰 생성
    public void createRefreshToken(MemberDTO member, String token, String accessToken) {
        TokenRedis tokenRedis=new TokenRedis(token, member.userId(), accessToken);
        tokenRedisRepository.save(tokenRedis);
    }

    //리프레시 토큰 삭제
    public void deleteRefreshToken(String email) {
        Optional<Member> member=memberRepository.findByEmail(email);
        member.ifPresent(m->tokenRedisRepository.deleteById(m.getUserId()));
    }

    //리프레시 토큰 유효성 확인
    public boolean isTokenValid(String refreshToken, Long userId) {
        Optional<TokenRedis>tokenRedis=tokenRedisRepository.findById(userId); //redis에서 userId로 리프레시 토큰 조회
        return tokenRedis.map(redisToken->redisToken.getRefreshToken().equals(refreshToken)).orElse(false);
    }

    public TokenRedisDTO getRefreshToken(Long userId){
        Optional<TokenRedis>tokenRedis=tokenRedisRepository.findById(userId);
        if(tokenRedis.isPresent()){
            TokenRedis token=tokenRedis.get();
            return TokenRedisDTO.toDTO(token);
        }else{
            return null;
        }
    }
}
