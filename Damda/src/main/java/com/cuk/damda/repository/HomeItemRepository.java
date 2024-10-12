package com.cuk.damda.repository;

import com.cuk.damda.domain.HomeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeItemRepository extends JpaRepository<HomeItem, Long> {
}
