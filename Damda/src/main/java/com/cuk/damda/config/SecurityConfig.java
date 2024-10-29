package com.cuk.damda.config;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

import com.cuk.damda.config.jwt.JwtAuthorizationFilter;
import com.cuk.damda.config.oauth.handler.OAuth2FailureHandler;
import com.cuk.damda.config.oauth.handler.OAuth2SuccessHandler;
import com.cuk.damda.config.oauth.OAuth2UserCustomService;
import com.cuk.damda.config.oauth.OAuthAuthorizationRequestBasedOnCookieRepository;
import java.util.Arrays;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity //Security Filter 등록
@RequiredArgsConstructor
public class SecurityConfig {
    private final OAuth2UserCustomService oAuth2UserCustomService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuthAuthorizationRequestBasedOnCookieRepository oAuthAuthorizationRequestBasedOnCookieRepository;
    private final OAuth2FailureHandler oAuth2FailureHandler;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public WebSecurityCustomizer configure(){ //스프링 시큐리티 기능 비활성화. 정적 리소스에 설정
        return (web)->web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers("/static/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) //csrf 토큰 비활성화(JWT 사용)
                .authorizeHttpRequests((requests)->requests
                        .requestMatchers("/resources/**","/static/**").permitAll() //인증 없어도 접근 허용
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/api/token").permitAll() //토큰 재발급 관련은 인증 없이 접근 가능하도록 설정
                        .requestMatchers("/api/**").permitAll()
                        .anyRequest().authenticated() //이 외에는 인증된 사용자만 접근 허용
                )
                //토큰 방식으로 인증 -> 폼 로그인, 세션 비활성화
                .sessionManagement((session)->session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                //헤더를 확인할 커스텀 추가
//                .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(configure -> configure
                    .authorizationEndpoint(authEndpoint -> authEndpoint
                            // Authorization 요청과 관련된 상태 저장
                            .authorizationRequestRepository(oAuthAuthorizationRequestBasedOnCookieRepository))
                    .failureHandler(oAuth2FailureHandler)
                    .successHandler(oAuth2SuccessHandler) // 인증 성공 시 실행할 핸들러
                    .userInfoEndpoint(userInfo -> userInfo
                            .userService(oAuth2UserCustomService)))
//                .exceptionHandling(user->user
//                        .defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
//                                new AntPathRequestMatcher("/api/**")))
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class) // JwtAuthorizationFilter 추가
                //cors 설정
                .cors(cors->cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Collections.singletonList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
