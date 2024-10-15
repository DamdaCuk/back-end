package com.cuk.damda.colllections.domain;

import com.cuk.damda.colllections.domain.Enum.ItemType;
import com.cuk.damda.colllections.domain.Enum.Rating;
import com.cuk.damda.global.domain.BaseEntity;
import com.cuk.damda.home.domain.Home;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="collections_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Collections extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long collectionsId;

    private Long itemId;
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
    public Collections(Long itemId, ItemType itemType, String itemTitle, String itemImg){
        this.itemId = itemId;
        this.itemType = itemType;
        this.itemTitle = itemTitle;
        this.itemImg=itemImg;
    }

    public static Collections create(Long itemId, ItemType itemType, String itemTitle, String itemImg){
        return Collections.builder()
                .itemId(itemId)
                .itemType(itemType)
                .itemTitle(itemTitle)
                .itemImg(itemImg)
                .build();
    }
}
