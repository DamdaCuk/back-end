package com.cuk.damda.member.domain;

import com.cuk.damda.item.domain.Category;
import com.cuk.damda.item.domain.Item;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "refresh_token_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Member member;

    @Builder
    public RefreshToken(String token, LocalDateTime expiryDate, Member member) {
        this.token = token;
        this.expiryDate = expiryDate;
        this.member = member;
    }

    public static RefreshToken create(String token, LocalDateTime expiryDate, Member member ) {
        return RefreshToken.builder()
                .token(token)
                .expiryDate(expiryDate)
                .member(member)
                .build();
    }
}
