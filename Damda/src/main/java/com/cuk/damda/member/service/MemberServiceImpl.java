package com.cuk.damda.member.service;

import com.cuk.damda.member.domain.Member;
import com.cuk.damda.member.repository.MemberRepository;
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


}
