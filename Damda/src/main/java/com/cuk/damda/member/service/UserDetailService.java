package com.cuk.damda.member.service;

import com.cuk.damda.member.domain.Member;
import com.cuk.damda.member.repository.MemberRepository;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    //email로 사용자 정보를 가져옴
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       return memberRepository.findByEmail(email)
                .orElseThrow(()->new IllegalArgumentException(email));
    }
}
