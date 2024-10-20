package com.cuk.damda.member.service;

import com.cuk.damda.member.domain.Member;
import com.cuk.damda.member.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Unexpected user"));
    }

    @Override
    public Member findByEmail(String email) { //이메일을 입력 받아 member 테이블에서 유저를 찾고 없으면 예외를 발생
        return memberRepository.findByEmail(email)
                .orElseThrow(()->new IllegalArgumentException("Unexpected user"));
    }

    @Override
    public Member saveMember(String email, String name, String provider){
        Optional<Member> existingMember = memberRepository.findByEmail(email);

        Member member = null;
        if(existingMember.isEmpty()) {
            member=Member.create(name, email, provider);
            memberRepository.save(member);
        }
        return member;
    }

}
