package com.cuk.damda.guestbook.repository;

import com.cuk.damda.guestbook.domain.Likes;
import com.cuk.damda.home.domain.Home;
import com.cuk.damda.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {
    Likes findByLikeGiverAndLikeReceiver(Member likeGiver, Home likeReceiver);
}
