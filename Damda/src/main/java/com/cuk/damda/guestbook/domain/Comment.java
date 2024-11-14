package com.cuk.damda.guestbook.domain;

import com.cuk.damda.home.domain.Home;
import com.cuk.damda.global.domain.BaseEntity;
import com.cuk.damda.member.domain.Member;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Member author;

    @Builder
    public Comment(String comment, Home home, Member author) {
        this.comment=comment;
        this.home=home;
        this.author=author;
    }

    public static Comment create(String comment, Home home, Member author) {
        return Comment.builder()
                .comment(comment)
                .home(home)
                .author(author)
                .build();
    }

    public void updateComment(String comment){
        this.comment=comment;
    }
}
