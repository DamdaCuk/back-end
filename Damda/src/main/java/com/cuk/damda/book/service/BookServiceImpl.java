//작동 되는 코드 쥬아~~~
package com.cuk.damda.book.service;

import com.cuk.damda.book.controller.response.BookListResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookServiceImpl {

    private final String apiUrl = "https://openapi.naver.com/v1/search/book.json";

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    public List<BookListResponse> searchBookByTitle(String title) {
        String encodedTitle;
        try {
            encodedTitle = URLEncoder.encode(title, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("검색어 인코딩 실패", e);
        }

        String requestUrl = apiUrl + "?query=" + encodedTitle + "&sort=sim";

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
        String response = get(requestUrl, requestHeaders);
        return parseBookListResponse(response);  // 파싱 후 결과 반환
    }

    private String get(String apiUrl, Map<String, String> requestHeaders) {
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return readBody(con.getInputStream());
            } else {
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            throw new RuntimeException("API URL 연결 실패: " + apiUrl, e);
        }
    }

    private String readBody(InputStream body) {
        InputStreamReader streamReader = new InputStreamReader(body);
        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();
            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }
            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는 데 실패했습니다.", e);
        }
    }

    private List<BookListResponse> parseBookListResponse(String responseBody) {
        List<BookListResponse> bookList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(responseBody);
            JsonNode items = root.path("items");

            // 여러 개의 책 정보를 리스트에 추가
            for (JsonNode item : items) {
                bookList.add(new BookListResponse(
                        item.path("title").asText(),
                        item.path("author").asText(),
                        item.path("publisher").asText(),
                        item.path("image").asText(),
                        item.path("description").asText()
                        // 출판사 정보 추가
                ));
            }
        } catch (IOException e) {
            throw new RuntimeException("응답 JSON 파싱 실패", e);
        }
        return bookList;
    }
}



//RestTemplate으로 해봤지만 안되는 코드....
/*import com.cuk.damda.book.controller.response.BookListResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookServiceImpl {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    //@Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;

    //@Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    // RestTemplate을 생성자 주입으로 받아오기
    public BookServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<BookListResponse> searchBookByTitle(String title) {
        // 요청 URL 생성
        String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
        URI uri = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com")
                .path("/v1/search/book.json")
                .queryParam("query", encodedTitle)
                .queryParam("sort", "sim")
                .encode()
                .build()
                .toUri();

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // API 호출 및 응답 받기
        ResponseEntity<String> response = restTemplate.exchange(uri, org.springframework.http.HttpMethod.GET, entity, String.class);
        System.out.println(response.getBody());
        // 응답 바디를 JSON으로 파싱하여 필요한 데이터 추출
        return parseBookListResponse(response.getBody());
    }

    private List<BookListResponse> parseBookListResponse(String responseBody) {
        List<BookListResponse> bookList = new ArrayList<>();
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode items = root.path("items");

            for (JsonNode item : items) {
                bookList.add(new BookListResponse(
                        item.path("title").asText(),
                        item.path("author").asText(),
                        item.path("image").asText(),
                        item.path("description").asText()
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException("응답 JSON 파싱 실패", e);
        }

        return bookList;
    }
}*/









