//작동 되는 코드 쥬아~~~
package com.cuk.damda.book.service;

import com.cuk.damda.book.controller.dto.BookDetailsDto;
import com.cuk.damda.book.controller.response.BookDetailsResponse;
import com.cuk.damda.book.domain.Book;
import com.cuk.damda.book.repository.BookRepository;
import com.cuk.damda.contents.domain.Contents;
import com.cuk.damda.contents.domain.Enum.ItemType;
import com.cuk.damda.contents.repository.ContentsRepository;
import com.cuk.damda.home.domain.Home;
import com.cuk.damda.home.repository.HomeRepository;
import com.cuk.damda.member.domain.Member;
import com.cuk.damda.member.repository.MemberRepository;
import com.cuk.damda.movie.controller.response.MovieDetailsResponse;
import com.cuk.damda.movie.domain.Movie;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

@RequiredArgsConstructor
@Slf4j
@Service
public class BookServiceImpl implements BookService {

    private final HomeRepository homeRepository;
    private final BookRepository bookRepository;
    private final ContentsRepository contentsRepository;
    private final MemberRepository memberRepository;

    private final String apiUrl = "https://openapi.naver.com/v1/search/book.json";

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    @Override
    @Transactional(readOnly = true)
    public List<BookDetailsDto> searchBookByTitle(String title, int start) {
        String encodedTitle;
        try {
            encodedTitle = URLEncoder.encode(title, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("검색어 인코딩 실패", e);
        }


        String requestUrl = apiUrl + "?query=" + encodedTitle + "&display=20&start=" + start + "&sort=sim";

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);

        String response = get(requestUrl, requestHeaders);
        return parseBookListResponse(response);  // 파싱 후 결과 반환
    }

    @Override
    @Transactional
    public void addBookContents(BookDetailsDto bookDto, String userEmail) {

        Member member = memberRepository.findByEmail(userEmail).
                orElseThrow(()->new IllegalArgumentException("유저를 찾을 수 없습니다."));
        Home home=member.getHome();
        if(home==null){
            throw new IllegalArgumentException("home을 찾을 수 없습니다");
        }

        //DB에 book 정보 탐색
        Book bookEntity = bookRepository.findByIsbn(bookDto.isbn()).orElse(null);

        if(bookEntity == null) {
            //없으면 api에서 정보탐색
            log.info("DB에 저장");

            Book apiEntity = Book.create(
                bookDto.isbn(),
                bookDto.title(),
                bookDto.author(),
                bookDto.publisher(),
                bookDto.image()
            );
            bookEntity = bookRepository.save(apiEntity);
        }

        Contents contents = Contents.create(
            bookEntity.getBookId(),
            ItemType.BOOK,
            bookEntity.getTitle(),
            bookEntity.getImage(),
            home
        );

        //중복 데이터 방지
        Contents existContent = contentsRepository.findByItemIdAndHomeAndItemType(bookEntity.getBookId(), home, ItemType.BOOK)
                .orElse(null);
        if(existContent != null) {
            throw new IllegalArgumentException("이미 저장된 컨텐츠 입니다.");
        }

        contentsRepository.save(contents);
    }

    @Override
    public Page<BookDetailsResponse> getBookDetailsList(String title, Pageable pageable) {
        Page<Book> detailsList = bookRepository.findByTitleContains(title, pageable);
        return detailsList.map(BookDetailsResponse::of);
    }

    /**
     *  API 처리
     */

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

    private List<BookDetailsDto> parseBookListResponse(String responseBody) {
        List<BookDetailsDto> bookList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(responseBody);
            JsonNode items = root.path("items");

            for (JsonNode item : items) {
                bookList.add(BookDetailsDto.from(
                        item.path("isbn").asText(),
                        item.path("title").asText(),
                        item.path("author").asText(),
                        item.path("publisher").asText(),
                        item.path("image").asText()
                ));
            }
        } catch (IOException e) {
            throw new RuntimeException("응답 JSON 파싱 실패", e);
        }
        return bookList;
    }

}



