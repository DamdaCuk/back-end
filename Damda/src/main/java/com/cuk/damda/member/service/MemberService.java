package com.cuk.damda.member.service;

import com.cuk.damda.member.controller.dto.MemberDTO;
import com.cuk.damda.member.domain.Member;

public interface MemberService {
    Member findById(Long id);
    MemberDTO findByEmail(String email);
    Member saveMember(String email, String name, String provider);
}
