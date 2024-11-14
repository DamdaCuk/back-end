package com.cuk.damda.home.service;

import com.cuk.damda.contents.domain.Contents;
import com.cuk.damda.contents.domain.Enum.ItemType;
import com.cuk.damda.contents.repository.ContentsRepository;
import com.cuk.damda.global.exception.exceptions.DBError;
import com.cuk.damda.global.exception.exceptions.UserNotFound;
import com.cuk.damda.home.controller.response.HomeResponse;
import com.cuk.damda.home.domain.Home;
import com.cuk.damda.home.repository.HomeRepository;
import com.cuk.damda.member.domain.Member;
import com.cuk.damda.member.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
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
            System.out.println(home);
            System.out.println(homeRepository.findByHomeId(home.getHomeId()));
            Optional<Member> findMember = memberRepository.findByEmail(userEmail);
            if (findMember.isEmpty()) {
                throw new UserNotFound();
            }

            Member member = findMember.get();
            System.out.println(home);
            member.updateHome(home);
            System.out.println(member);
            System.out.println(member.getHome());
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
    public List<HomeResponse> searchHome(String title, String contentType) {
        List<Contents> contentsList = contentsRepository.findByItemTitleAndItemType(title, ItemType.valueOf(contentType));
        return contentsList.stream()
                .map(Contents::getHome)
                .distinct()
                .map(home -> new HomeResponse(home.getHomeId(),home.getHomeName(), home.getLikes()))
                .collect(Collectors.toList());
    }
}
