package com.cuk.damda.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Collections;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class TokenProvider {
    private static final long ACCESS_TOKEN_EXPIRE_TIME_IN_MILLISECONDS = 1000 * 60 * 30; // 30min
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 14; //리프레시 토큰 만료 기간 : 14일

    private final JwtProperties jwtProperties;
    @Value("${jwt.secretKey}")
    private String secret;
    private Key key;

    @PostConstruct
    public void init(){
        byte[] key= Decoders.BASE64.decode(secret);
        this.key=Keys.hmacShaKeyFor(key);
    }

    public String makeToken(Authentication authentication, Long userId){
        Date now=new Date();
        Date expiryDate=new Date(now.getTime()+ACCESS_TOKEN_EXPIRE_TIME_IN_MILLISECONDS);

        return Jwts.builder()
                .setSubject(authentication.getName()) //email로 토큰 생성
                .setIssuedAt(now)
                .claim("userId", userId) // 클레임에 userId 추가
                .setIssuer(jwtProperties.getIssuer())
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256) //이게 보안성 높아짐
                .compact();
    }

    //리프레시 토큰 생성
    public String makeRefreshToken(Authentication authentication, Long userId){
        Date now=new Date();
        Date expiryDate=new Date(now.getTime()+REFRESH_TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(now)
                .claim("userId", userId) // 클레임에 userId 추가
                .setIssuer(jwtProperties.getIssuer())
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    //JWT 토큰 유효성 검증 메서드
    public boolean validateToken(String token) {
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) { //확인용. 에러 처리X
            log.error("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.error("만료된 액세스 토큰 사용!!", e);
            return false;
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty.", e);
        }
        return false;
    }

    //토큰 기반으로 유저 ID를 가져오는 메서드
    public Long getUserId(String token){
        Claims claims=getClaims(token);
        return claims.get("userId",Long.class);
    }

    private Claims getClaims(String token){
        return Jwts.parserBuilder()//클레임 조회
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //토큰 기반으로 인증 정보를 가져옴
    public Authentication getAuthentication(String token){
        Claims claims=getClaims(token);

        Long userId=claims.get("userId",Long.class);
        String email=claims.getSubject();

        if(email==null){
            System.out.println("email is null");
        }

        UserDetails user=new User(email,"",Collections.emptyList()); //유저 정보를 담아 인증 객체 생성
        return new UsernamePasswordAuthenticationToken(user, "",Collections.emptyList());
    }
}
