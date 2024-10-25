package com.cuk.damda.config.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@RequiredArgsConstructor
@Component
@ConfigurationProperties("jwt") //자바 클래스에 프로퍼티 값을 가져와서 사용하는 애노테이션
public class JwtProperties {
    private String issuer;
    private String secretKey;
}
