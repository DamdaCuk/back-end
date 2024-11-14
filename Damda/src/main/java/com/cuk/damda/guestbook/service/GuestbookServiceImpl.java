package com.cuk.damda.guestbook.service;

import com.cuk.damda.global.exception.exceptions.HomeNotFoundException;
import com.cuk.damda.global.exception.exceptions.UserNotFoundException;
import com.cuk.damda.guestbook.controller.request.LikeRequest;
import com.cuk.damda.guestbook.domain.Likes;
import com.cuk.damda.guestbook.repository.GuestbookRepository;
import com.cuk.damda.guestbook.repository.LikesRepository;
import com.cuk.damda.home.domain.Home;
import com.cuk.damda.home.repository.HomeRepository;
import com.cuk.damda.member.domain.Member;
import com.cuk.damda.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GuestbookServiceImpl implements GuestbookService {
    private final GuestbookRepository guestbookRepository;
    private final LikesRepository likesRepository;
    private final MemberRepository memberRepository;
    private final HomeRepository homeRepository;

    @Transactional
    @Override
    public void addLike(LikeRequest likeRequest, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        Home home=homeRepository.findById(likeRequest.homeId())
                .orElseThrow(HomeNotFoundException::new);

        Likes likeFind = likesRepository.findByLikeGiverAndLikeReceiver(member, home)
                .orElseThrow(()->new IllegalArgumentException("좋아요를 누른 기록이 존재하지 않습니다."));

        if(likeFind!=null) {
            throw new RuntimeException("이미 좋아요를 눌렀습니다.");
        }else{ //좋아요를 누르지 않은 경우 좋아요 누르기
            Likes like=new Likes(member, home);
            likesRepository.save(like); //좋아요 테이블에 저장(좋아요를 누른 사람, 좋아요가 눌린 홈)

            home.incrementLikes(); //방명록 좋아요 수 업데이트
        }

    }

    @Override
    @Transactional(readOnly=true)
    public boolean isLike(LikeRequest likeRequest, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        Home home=homeRepository.findById(likeRequest.homeId())
                .orElseThrow(HomeNotFoundException::new);

        Likes likeFind = likesRepository.findByLikeGiverAndLikeReceiver(member, home)
                .orElseThrow(()->new IllegalArgumentException("좋아요를 누른 기록이 존재하지 않습니다."));
        return likeFind != null;
    }

    @Override
    public void deleteLike(LikeRequest likeRequest, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        Home home=homeRepository.findById(likeRequest.homeId())
                .orElseThrow(HomeNotFoundException::new);


        Likes likeFind = likesRepository.findByLikeGiverAndLikeReceiver(member, home)
                .orElseThrow(()->new IllegalArgumentException("좋아요를 누른 기록이 존재하지 않습니다."));

        if(likeFind==null) {
            throw new RuntimeException("좋아요를 누른 기록이 존재하지 않습니다.");
        }else{ //좋아요를 누른 기록이 있는 경우 좋아요 취소 가능
            likesRepository.delete(likeFind);
            home.decrementLikes(); //방명록 좋아요 수 업데이트
        }
    }
}
