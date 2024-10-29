package com.cuk.damda.home.service;

import com.cuk.damda.global.exception.exceptions.DBError;
import com.cuk.damda.global.exception.exceptions.UserNotFound;
import com.cuk.damda.home.domain.Home;
import com.cuk.damda.home.repository.HomeRepository;
import com.cuk.damda.member.domain.Member;
import com.cuk.damda.member.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HomeServiceImpl implements HomeService {

    private final HomeRepository homeRepository;
    private final MemberRepository memberRepository;
    private final EntityManager em;

    @Override
    @Transactional
    public void createHome(String homeName, String userEmail) {
        Home home=Home.createHome(homeName);
        Home makeHome=homeRepository.save(home);
        if(!makeHome.getHomeName().equals(homeName)) {
            throw new DBError("create home failed");
        }

        Optional<Member> findMember=memberRepository.findByEmail(userEmail);
        if(findMember.isPresent()) {
            throw new UserNotFound();
        }
        Member member=em.find(Member.class, findMember.get().getUserId());
        member.setHome(home);
    }
}
