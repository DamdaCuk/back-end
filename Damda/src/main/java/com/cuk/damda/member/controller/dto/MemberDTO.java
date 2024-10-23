package com.cuk.damda.member.controller.dto;

import com.cuk.damda.member.domain.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDTO {
    private Long userId;
    private String username;
    private String email;
    private String provider; //공급자
    private Long homeId;

    public MemberDTO() {}

    public MemberDTO(Long userId, String username, String email, String provider) {
        this.userId=userId;
        this.username=username;
        this.email=email;
        this.provider=provider;
    }

    public MemberDTO(Long userId, String username, String email, String provider, Long homeId) {
        this.userId=userId;
        this.username=username;
        this.email=email;
        this.provider=provider;
        this.homeId=homeId;
    }

    public static MemberDTO toDTO(Member member) {
        MemberDTO dto = new MemberDTO();
        dto.setUserId(member.getUserId());
        dto.setUsername(member.getUsername());
        dto.setEmail(member.getEmail());
        dto.setProvider(member.getProvider());
        if(member.getHome()!=null) {
            dto.setHomeId(member.getHome().getHomeId());
        }else{
            dto.setHomeId(0L);
        }
        return dto;
    }

    public static Member toEntity(MemberDTO dto) {
        return Member.create(dto.username, dto.email, dto.provider);
    }
}
