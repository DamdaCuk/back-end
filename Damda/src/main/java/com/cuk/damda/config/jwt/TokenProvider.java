package com.cuk.damda.config.jwt;

import com.cuk.damda.member.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
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

    public String makeToken(Authentication authentication){
        Date now=new Date();
        Date expiryDate=new Date(now.getTime()+ACCESS_TOKEN_EXPIRE_TIME_IN_MILLISECONDS);

        return Jwts.builder()
                .setSubject(authentication.getName()) //email로 토큰 생성
                .setIssuedAt(now)
                .setIssuer(jwtProperties.getIssuer())
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256) //이게 보안성 높아짐
                .compact();
    }

    //리프레시 토큰 생성
    public String makeRefreshToken(Authentication authentication){
        Date now=new Date();
        Date expiryDate=new Date(now.getTime()+REFRESH_TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(now)
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
        } catch (Exception e){ //복호화 과정에서 에러가 나면 유효하지 않은 토큰
            return false;
        }
    }

    //토큰 기반으로 유저 ID를 가져오는 메서드
    public Long getUserId(String token){
        Claims claims=getClaims(token);
        return claims.get("userId",Long.class);
    }

    //토큰 기반으로 유저 email를 가져오는 메서드
    public String getUserEmail(String token){
        Claims claims=getClaims(token);
        return claims.get("email",String.class);
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
        Claims claims=Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        UserDetails user=new User(claims.getSubject(),"",Collections.emptyList());
        return new UsernamePasswordAuthenticationToken(user, "",Collections.emptyList());
    }
}
