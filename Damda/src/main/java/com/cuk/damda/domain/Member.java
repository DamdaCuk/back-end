package com.cuk.damda.domain;

import com.cuk.damda.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@ToString
@Table(name="member_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String username;

    private String email;

    private String password;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_id", nullable = false)
    private Home home;

    @Builder
    private Member(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
    
    public static Member create(String username, String email, String password) {
        return Member.builder()
                .username(username)
                .email(email)
                .password(password)
                .build();
    }
}
