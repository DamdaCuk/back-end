package com.cuk.damda.member.domain;

import com.cuk.damda.global.domain.BaseEntity;
import com.cuk.damda.home.domain.Home;
import jakarta.persistence.*;
import java.util.Collection;
import java.util.List;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@ToString
@Table(name="member_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String username;

    private String email;

    private String provider; //공급자

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_id") //null 허용(처음 계정을 만든 사용자)
    private Home home;

    @Builder
    private Member(String username, String email, String provider) {
        this.username = username;
        this.email = email;
        this.provider=provider;
    }
    
    public static Member create(String username, String email, String provider) {
        return Member.builder()
                .username(username)
                .email(email)
                .provider(provider)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return ""; //일반 로그인 진행 X -> "" return 하도록
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
