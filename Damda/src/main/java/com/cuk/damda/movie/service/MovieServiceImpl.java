package com.cuk.damda.movie.service;

import com.cuk.damda.movie.controller.response.MovieDetailsResponse;
import com.cuk.damda.movie.controller.response.MovieListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private final RestTemplate movieRestTemplate;

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
    @Override
    public List<MovieListResponse> getMovieList(String title, int page) {
        //API 주소
        String url = "/search/movie?query=" + title + "&language=ko-kr&page=" + page;

        ResponseEntity<Map> apiResponse = movieRestTemplate.getForEntity(url, Map.class);

        Map<String, Object> movieData = apiResponse.getBody();

        //API 검색 결과
        List<Map<String, Object>> movieList = (List<Map<String, Object>>) movieData.get("results");

        //영화 목록 반환
        List<MovieListResponse> resultList = new ArrayList<>();
        for(Map<String, Object> movie: movieList){
            // TODO :: posterPath는 null값이 있을 수 있으므로 예외처리 필요
            String posterPath = null;
            if(movie.get("poster_path") != null){
                posterPath = (String) movie.get("poster_path");
            }

            resultList.add(MovieListResponse.from((int)movie.get("id"), (String) movie.get("title"), posterPath));

        }
        return resultList;
    }


    /**
     * API에서 영화 세부정보
     * @param movieId
     */

}
