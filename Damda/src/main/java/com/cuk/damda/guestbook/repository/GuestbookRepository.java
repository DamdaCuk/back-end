package com.cuk.damda.guestbook.repository;

import com.cuk.damda.guestbook.domain.Guestbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestbookRepository extends JpaRepository<Guestbook, Long> {
}
