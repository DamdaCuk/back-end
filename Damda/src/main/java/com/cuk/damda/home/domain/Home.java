package com.cuk.damda.home.domain;

import com.cuk.damda.homeItem.domain.HomeItem;
import com.cuk.damda.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="home")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Home extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long homeId;

    private Long knock;
    private Long likes;

    @OneToMany(mappedBy = "home", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<HomeItem> homeItems = new HashSet<>();

    @Builder
    public Home(Long knock, Long likes) {
        this.knock=knock;
        this.likes=likes;
    }

    public static Home create(Long knock, Long likes) {
        return Home.builder()
                .knock(knock)
                .likes(likes)
                .build();
    }
}
