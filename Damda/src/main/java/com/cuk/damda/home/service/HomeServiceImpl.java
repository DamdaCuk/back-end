package com.cuk.damda.home.service;

import com.cuk.damda.book.repository.BookRepository;
import com.cuk.damda.contents.domain.Contents;
import com.cuk.damda.contents.domain.Enum.ItemType;
import com.cuk.damda.contents.repository.ContentsRepository;
import com.cuk.damda.global.exception.exceptions.DBError;
import com.cuk.damda.global.exception.exceptions.UserNotFound;
import com.cuk.damda.home.controller.response.HomeListResponse;
import com.cuk.damda.home.domain.Home;
import com.cuk.damda.home.repository.HomeRepository;
import com.cuk.damda.member.domain.Member;
import com.cuk.damda.member.repository.MemberRepository;
import com.cuk.damda.movie.repository.MovieRepository;
import com.cuk.damda.music.repository.MusicRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HomeServiceImpl implements HomeService {

    private final HomeRepository homeRepository;
    private final MemberRepository memberRepository;
    private final EntityManager em;
    private final ContentsRepository contentsRepository;

    @Override
    @Transactional
    public void memberInsertHome(Home home, String userEmail) throws UserNotFound {
        try {
            Optional<Member> findMember = memberRepository.findByEmail(userEmail);
            if (findMember.isEmpty()) {
                throw new UserNotFound();
            }

            Member member = findMember.get();
            member.updateHome(home);
            memberRepository.save(member);
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    @Transactional
    public Home createHome(String homeName) throws UserNotFound {
        Home home = Home.createHome(homeName);
        Home makeHome = homeRepository.save(home);
        em.flush();
        if (!makeHome.getHomeName().equals(homeName)) {
            throw new DBError("create home failed");
        }
        return home;
    }

    @Override
    public Page<HomeListResponse> searchHomes(Long itemId, String contentType, Pageable pageable) {
        Page<Contents> contentsPage = contentsRepository.findByItemIdAndItemType(itemId, ItemType.valueOf(contentType), pageable);
        return contentsPage.map(contents -> HomeListResponse.from(
                contents.getHome().getHomeId(),
                contents.getHome().getHomeName(),
                contents.getHome().getLikes()
        ));
    }
}
