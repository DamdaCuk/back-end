package com.cuk.damda.home.repository;

import com.cuk.damda.home.domain.Home;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeRepository extends JpaRepository<Home, Long> {
}
