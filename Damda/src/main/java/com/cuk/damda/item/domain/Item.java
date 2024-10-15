package com.cuk.damda.item.domain;

import com.cuk.damda.global.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@ToString
@Table(name="item_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Item extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;
    @NotNull
    private String url;
    @NotNull
    private Category category;

    @Builder
    public Item(String url, Category category) {
        this.url = url;
        this.category = category;
    }

    public static Item create(String url, Category category) {
        return Item.builder()
                .url(url)
                .category(category)
                .build();
    }

}
