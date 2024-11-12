package com.cuk.damda.guestbook.repository;

import com.cuk.damda.guestbook.controller.response.GetCommentsResponse;
import com.cuk.damda.guestbook.domain.Comment;
import com.cuk.damda.home.domain.Home;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByHome(Home home);
}
