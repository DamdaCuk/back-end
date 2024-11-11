package com.cuk.damda.guestbook.domain;

import com.cuk.damda.home.domain.Home;
import com.cuk.damda.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="comment_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long commentId;
    @Column(columnDefinition = "TEXT")
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_id", nullable = false)
    private Home home;

    @Builder
    public Comment(String comment, Home home){
        this.comment=comment;
        this.home=home;
    }

    public static Comment create(String comment, Home home){
        return Comment.builder()
                .comment(comment)
                .home(home)
                .build();
    }
}
