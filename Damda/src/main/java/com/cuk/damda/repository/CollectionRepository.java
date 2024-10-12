package com.cuk.damda.repository;

import com.cuk.damda.domain.Collections;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionRepository extends JpaRepository<Collections, Long> {
}
