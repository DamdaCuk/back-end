package com.cuk.damda.contents.repository;

import com.cuk.damda.contents.domain.Contents;
import com.cuk.damda.contents.domain.Enum.ItemType;
import com.cuk.damda.home.domain.Home;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContentsRepository extends JpaRepository<Contents, Long> {
    Optional<Contents> findByItemIdAndHomeAndItemType(Long itemId, Home home, ItemType itemType);
    Optional<Contents> findByHome_HomeIdAndContentsId(Long homeId, Long contentsId);
}
