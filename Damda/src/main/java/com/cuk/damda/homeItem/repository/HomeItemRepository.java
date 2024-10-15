package com.cuk.damda.homeItem.repository;

import com.cuk.damda.homeItem.domain.HomeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeItemRepository extends JpaRepository<HomeItem, Long> {
}
