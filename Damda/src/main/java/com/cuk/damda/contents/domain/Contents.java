package com.cuk.damda.contents.domain;

import com.cuk.damda.contents.domain.Enum.ItemType;
import com.cuk.damda.contents.domain.Enum.Rating;
import com.cuk.damda.global.domain.BaseEntity;
import com.cuk.damda.home.domain.Home;
import jakarta.persistence.*;
import lombok.*;

import java.util.Optional;

@Entity
@Table(name="contents_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Contents extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contentsId;

    private Long itemId;

    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    private String itemTitle;
    private String itemImg;

    @Column(columnDefinition = "TEXT")
    private String review;

    private Rating rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_id", nullable = false)
    private Home home;

    @Builder
    public Contents(Long itemId, ItemType itemType, String itemTitle, String itemImg, Home home){
        this.itemId = itemId;
        this.itemType = itemType;
        this.itemTitle = itemTitle;
        this.itemImg=itemImg;
        this.home=home;
    }

    public static Contents create(Long itemId, ItemType itemType, String itemTitle, String itemImg, Home home){
        return Contents.builder()
                .itemId(itemId)
                .itemType(itemType)
                .itemTitle(itemTitle)
                .itemImg(itemImg)
                .home(home)
                .build();
    }

    public void addReview(String review, Rating rating) {
        this.review = review;
        this.rating = rating;
    }
    public void review(String review) {
        this.review = review;
    }
    public void rating(Rating rating) {
        this.rating = rating;
    }
}
