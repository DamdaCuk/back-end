package com.cuk.damda.config.oauth.handler;

import static com.cuk.damda.config.oauth.OAuthAuthorizationRequestBasedOnCookieRepository.MODE_PARAM_COOKIE_NAME;

import com.cuk.damda.config.jwt.TokenProvider;
import com.cuk.damda.config.oauth.OAuth2UserPrincipal;
import com.cuk.damda.config.oauth.OAuthAuthorizationRequestBasedOnCookieRepository;
import com.cuk.damda.config.oauth.user.OAuth2Provider;
import com.cuk.damda.config.oauth.user.unlink.OAuth2UserUnlinkManager;
import com.cuk.damda.member.controller.dto.MemberDTO;
import com.cuk.damda.member.domain.Member;
import com.cuk.damda.member.service.MemberService;
import com.cuk.damda.member.service.RefreshTokenService;
import com.cuk.damda.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public static final Duration ACCESS_TOKEN_DURATION=Duration.ofDays(1);

    private final TokenProvider tokenProvider;
    private final OAuthAuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
    private final RefreshTokenService refreshTokenService;
    private final MemberService memberService;
    private final OAuth2UserUnlinkManager oAuth2UserUnlinkManager;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException{
        String targetUrl;
        targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            return;
        }
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    @Transactional
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {

        Optional<String> redirectUri = CookieUtil.getCookie(request, authorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        String mode = CookieUtil.getCookie(request, MODE_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse("");

        OAuth2UserPrincipal principal = getOAuth2UserPrincipal(authentication);

        if (principal == null) {
            return UriComponentsBuilder.fromUriString(targetUrl)
                    .queryParam("error", "Login failed")
                    .build().toUriString();
        }

        String email=authentication.getName();
        if ("login".equalsIgnoreCase(mode)) {
//            log.info("email={}, name={}, accessToken={}",
//                    principal.getUserInfo().getEmail(),
//                    principal.getUserInfo().getName(),
//                    principal.getUserInfo().getAccessToken()
//            );

            MemberDTO memberDTO=memberService.findByEmail(email); //회원가입 여부 확인
            if(memberDTO==null) {
                //로그인한 유저(멤버) DB에 저장
                Member member = memberService.saveMember( //회원가입 진행
                        principal.getUserInfo().getEmail(),
                        principal.getUserInfo().getName(),
                        principal.getUserInfo().getProvider().name());
                memberDTO=MemberDTO.toDTO(member);
            }
            Long userId=memberDTO.userId();

            String accessToken = tokenProvider.makeToken(authentication, userId);
            String refreshToken = tokenProvider.makeRefreshToken(authentication, userId);

            //리프레시 토큰 레디스에 저장
            refreshTokenService.createRefreshToken(memberDTO, refreshToken, accessToken);

            // 액세스 토큰을 쿠키에 저장
            addAccessTokenToCookie(request, response, accessToken);

            return UriComponentsBuilder.fromUriString(targetUrl)
                    .queryParam("access_token", accessToken)
                    .build().toUriString();

        } else if ("unlink".equalsIgnoreCase(mode)) {

            String accessToken = principal.getUserInfo().getAccessToken();
            OAuth2Provider provider = principal.getUserInfo().getProvider();
            String userEmail=principal.getUserInfo().getEmail();

            oAuth2UserUnlinkManager.unlink(provider, accessToken, userEmail, request, response);

            //인증 관련 설정값 제거
            clearAuthenticationAttributes(request, response);

            return UriComponentsBuilder.fromUriString(targetUrl)
                    .build().toUriString();
        }

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", "Login failed")
                .build().toUriString();
    }

    private OAuth2UserPrincipal getOAuth2UserPrincipal(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2UserPrincipal) {
            return (OAuth2UserPrincipal) principal;
        }
        return null;
    }

    // 인증 관련 설정값, 쿠키 제거
    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    // 액세스 토큰을 쿠키에 저장
    private void addAccessTokenToCookie(HttpServletRequest request, HttpServletResponse response, String accessToken){
        int cookieMaxAge = (int) ACCESS_TOKEN_DURATION.toSeconds();
        CookieUtil.deleteCookie(request, response, "access_token");
        CookieUtil.addCookie(response, "access_token", accessToken, cookieMaxAge);
    }
}
