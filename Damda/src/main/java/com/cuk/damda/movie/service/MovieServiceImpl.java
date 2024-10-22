package com.cuk.damda.movie.service;

import com.cuk.damda.movie.controller.response.MovieDetailsResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class MovieServiceImpl implements MovieService {
    @Autowired
    private final RestTemplate movieRestTemplate;

    //일단 api에서 불러오니까 repo 필요없 --> 주로 DB에서 불러올듯
    @Override
    public MovieDetailsResponse getMovieDetails(int movieId){
        // TODO :: 메소드 추출(해당 아이디가 DB에 없을때만 호출)
        //API 주소 생성
        String url = "/movie/" + movieId + "?language=ko-kr&append_to_response=credits";
        //영화 정보 받아오기
        ResponseEntity<Map> apiResponse = movieRestTemplate.getForEntity(url, Map.class);

        Map<String, Object> movieData = apiResponse.getBody();

        //제목
        String title = movieData.get("title").toString();
        //포스터 이미지
        String posterPath = movieData.get("poster_path").toString();
        //장르 리스트
        List<Map<String, Object>> genresList = (List<Map<String, Object>>) movieData.get("genres");

        String genres = null;

        for(Map<String, Object> genre : genresList){
            if(genres == null){
                genres = genre.get("name").toString();
            }
            else{
                genres = genres + ", " + genre.get("name").toString() ;
            }
        }


        //[[Credits]]
        Map<String, Object> credits = (Map<String, Object>) movieData.get("credits");

        //감독
        List<Map<String, Object>> crewList = (List<Map<String, Object>>) credits.get("crew");

        String director = null;

        for(Map<String, Object> crew : crewList){
            String job = (String)crew.get("job");
            if("Director".equals(job)){
                if(director == null){
                    director = crew.get("name").toString();
                }
                else{
                    director = director + ", " + crew.get("name").toString() ;
                }

            }
        }
        //배우 리스트
        List<Map<String, Object>> castList = (List<Map<String, Object>>) credits.get("cast");

        String casts = null;

        for(int i = 0; i < Math.min(castList.size(), 4); i++){
            Map<String, Object> castMember = castList.get(i);
            if(casts == null){
                casts = castMember.get("name").toString();
            }
            else{
                casts = casts + ", " + castMember.get("name").toString() ;
            }

        }

        //DTO Response
        return MovieDetailsResponse.from(title, posterPath, director, casts, genres);
    }

    //영화 검색결과 리스트
    //컨텐츠에 저장되면->movie db에도 저장
}
