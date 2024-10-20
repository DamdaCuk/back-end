package com.cuk.damda.member.repository;

import com.cuk.damda.member.domain.RefreshToken;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.member.userId=:memberId")
    void deleteByMemberId(@Param("memberId") Long memberId);

}
