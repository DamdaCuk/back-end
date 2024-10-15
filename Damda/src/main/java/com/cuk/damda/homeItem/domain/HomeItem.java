package com.cuk.damda.homeItem.domain;

import com.cuk.damda.item.domain.Item;
import com.cuk.damda.global.domain.BaseEntity;
import com.cuk.damda.home.domain.Home;
import jakarta.persistence.*;
import lombok.*;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="home_item_tb")
@Getter
@ToString
public class HomeItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long homeItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_id", nullable = false)
    private Home home;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;
}
