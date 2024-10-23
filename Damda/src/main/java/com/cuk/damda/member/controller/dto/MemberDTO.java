package com.cuk.damda.member.controller.dto;

import com.cuk.damda.member.domain.Member;
import lombok.Getter;
import lombok.Setter;

public record MemberDTO(
        Long userId,
        String username,
        String email,
        String provider, //공급자
        Long homeId
) {
    public static MemberDTO of(Long userId, String username, String email, String provider, Long homeId) {
        return new MemberDTO(userId, username, email, provider, homeId);
    }

    public static MemberDTO of(Long userId, String username, String email, String provider) {
        return new MemberDTO(userId, username, email, provider, null);
    }

    public static MemberDTO toDTO(Member member) {
        return new MemberDTO(
                member.getUserId(),
                member.getUsername(),
                member.getEmail(),
                member.getProvider(),
                member.getHome() != null ? member.getHome().getHomeId() : null
        );
    }

    public Member toEntity() {
        return Member.create(
                username, email, provider
        );
    }
}
