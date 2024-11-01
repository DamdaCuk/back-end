package com.cuk.damda.guestbook.domain;

import com.cuk.damda.home.domain.Home;
import com.cuk.damda.member.domain.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "likes_tb")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Likes {
    @Id
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member likeGiver;
    @ManyToOne(fetch = FetchType.LAZY)
    private Home likeReceiver;

    @Builder
    public Likes(Member likeGiver, Home likeReceiver) {
        this.likeGiver=likeGiver;
        this.likeReceiver=likeReceiver;
    }

    public static Likes addLike(Member likeGiver, Home likeReceiver){
        return Likes.builder()
                .likeGiver(likeGiver)
                .likeReceiver(likeReceiver)
                .build();
    }
}
