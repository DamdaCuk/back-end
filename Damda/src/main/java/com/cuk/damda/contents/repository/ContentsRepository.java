package com.cuk.damda.contents.repository;

import com.cuk.damda.contents.domain.Contents;
import com.cuk.damda.contents.domain.Enum.ItemType;
import com.cuk.damda.home.domain.Home;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContentsRepository extends JpaRepository<Contents, Long> {
    Optional<Contents> findByItemIdAndHomeAndItemType(Long itemId, Home home, ItemType itemType);
    Slice<Contents> findByHomeAndItemType(Pageable pageable, Home home, ItemType itemType);

    Page<Contents> findByItemTitleAndItemType(String title, ItemType itemType, Pageable pageable);
}
