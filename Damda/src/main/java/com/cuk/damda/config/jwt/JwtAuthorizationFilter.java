package com.cuk.damda.config.jwt;

import com.cuk.damda.global.exception.exceptions.JwtValidationException;
import com.cuk.damda.member.service.TokenService;
import com.cuk.damda.util.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String accessToken = resolveToken(request);

        //액세스 토큰이 없을 경우 필터 체인 계속 진행
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try{
            //액세스 토큰 검증 및 필요시 재발급
            String validatedAccessToken=tokenService.validateAndRefreshToken(accessToken);

            if(!validatedAccessToken.equals(accessToken)){ //다를경우 액세스 토큰 쿠키에 저장
                CookieUtil.addCookie(response, "access_token", validatedAccessToken, 30 * 60);
            }

            //유효한 액세스 토큰에 대한 인증 정보를 설정
            Authentication authentication=tokenProvider.getAuthentication(validatedAccessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e){
            //토큰 검증 실패시 응답 처리
            e.printStackTrace();
            throw new JwtValidationException("토큰 검증에 실패했습니다.");
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for(Cookie cookie:cookies){
                if("access_token".equals(cookie.getName())){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
