package com.cuk.damda.member.service;

import com.cuk.damda.member.domain.Member;

public interface MemberService {
    Member findById(Long id);
    Member findByEmail(String email);
}
