package com.cuk.damda.movie.service;

import com.cuk.damda.contents.domain.Contents;
import com.cuk.damda.contents.domain.Enum.ItemType;
import com.cuk.damda.contents.repository.ContentsRepository;
import com.cuk.damda.home.domain.Home;
import com.cuk.damda.home.repository.HomeRepository;
import com.cuk.damda.movie.controller.response.MovieDetailsResponse;
import com.cuk.damda.movie.controller.response.MovieListResponse;
import com.cuk.damda.movie.domain.Movie;
import com.cuk.damda.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class MovieServiceImpl implements MovieService {

    private final RestTemplate movieRestTemplate;
    private final MovieRepository movieRepository;
    private final ContentsRepository contentsRepository;
    private final HomeRepository homeRepository;

    /**
     * contents에 영화 등록
     * @param apiId
     */
    @Override
    @Transactional
    public void addMovieContents(int apiId) {
        // TODO :: home 테스트 용 코드 -> 추후 수정
        Home testHome = homeRepository.findByHomeId(2L)
                .orElseThrow(() -> new IllegalArgumentException("home을 찾을 수 없습니다."));

        MovieDetailsResponse movieDetails;
        //DB에서 movie 정보 탐색
        Movie movieEntity = movieRepository.findByApiId(apiId);

        if(movieEntity == null){
            //없으면 api에서 정보탐색
            log.info("API에서 탐색!!");
            movieDetails = getMovieDetails(apiId);

            Movie apiEntity = Movie.create(
                    apiId,
                    movieDetails.director(),
                    movieDetails.actor(),
                    movieDetails.genre(),
                    movieDetails.title(),
                    movieDetails.posterPath()
            );
            movieEntity = movieRepository.save(apiEntity);
        }

        Contents contents = Contents.create(
            movieEntity.getMovieId(),
            ItemType.MOVIE,
            movieEntity.getTitle(),
            movieEntity.getPoster(),
            testHome
        );

        //중복 데이터 방지
        Contents existContent = contentsRepository.findByItemIdAndHomeAndItemType(movieEntity.getMovieId(), testHome, ItemType.MOVIE).orElse(null);
        if(existContent != null){
            throw new IllegalArgumentException("이미 저장된 컨텐츠 입니다.");
        }

        contentsRepository.save(contents);
    }

    /**
     * 제목으로 영화 리스트 조회(외부 api)
     * @param title
     * @param page
     * @return 영화 리스트
     */
    @Override
    @Transactional(readOnly = true)
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
        // TODO :: 가끔 api가 리스트 못 불러올 때 있는듯 --> 빈 리스트 반환받는 경우 있음
        return resultList;
    }

    /**
     * contents에서 영화 삭제
     * @param contentsId
     */
    @Override
    @Transactional
    public void deleteMovieContents(Long contentsId) {
        Contents deleteContents = contentsRepository.findById(contentsId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 컨텐츠입니다."));
        contentsRepository.delete(deleteContents);
    }

    /**
     * 영화 상세 정보 조회(외부 api)
     * @param apiId
     * @return
     */
    public MovieDetailsResponse getMovieDetails(int apiId) {
        //API 주소 생성
        String url = "/movie/" + apiId + "?language=ko-kr&append_to_response=credits";
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



}
