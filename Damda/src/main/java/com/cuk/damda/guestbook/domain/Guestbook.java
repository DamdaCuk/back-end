package com.cuk.damda.guestbook.domain;

import com.cuk.damda.home.domain.Home;
import com.cuk.damda.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="guest_book_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Guestbook extends BaseEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long guestbookId;
    @Column(columnDefinition = "TEXT")
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_id", nullable = false)
    private Home home;

    @Builder
    public Guestbook(String comment){
        this.comment=comment;
    }

    public static Guestbook create(String comment){
        return Guestbook.builder()
                .comment(comment)
                .build();
    }
}
