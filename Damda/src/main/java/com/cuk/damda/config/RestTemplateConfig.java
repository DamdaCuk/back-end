package com.cuk.damda.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpClient;
import java.time.Duration;

@RequiredArgsConstructor
@Configuration
public class RestTemplateConfig {

    @Value("${tmdb.api.key}")
    private String tmdbApiKey;

    //영화 API
    @Bean
    public RestTemplate movieRestTemplate(RestTemplateBuilder builder) {
        return builder
                .rootUri("https://api.themoviedb.org/3")  //호출할 API 서비스 도메인 URL
                .defaultHeader("Authorization","Bearer " + tmdbApiKey)    //API key
                .setConnectTimeout(Duration.ofSeconds(3))
                .setReadTimeout(Duration.ofSeconds(3))
                //.additionalInterceptors()
                .build();
    }

}
