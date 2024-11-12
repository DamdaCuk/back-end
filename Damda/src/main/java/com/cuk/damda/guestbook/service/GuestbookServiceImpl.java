package com.cuk.damda.guestbook.service;

import com.cuk.damda.global.exception.exceptions.DBError;
import com.cuk.damda.guestbook.controller.request.CommentRequest;
import com.cuk.damda.guestbook.domain.Comment;
import com.cuk.damda.guestbook.repository.CommentRepository;
import com.cuk.damda.home.domain.Home;
import com.cuk.damda.home.repository.HomeRepository;
import com.cuk.damda.member.domain.Member;
import com.cuk.damda.member.repository.MemberRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GuestbookServiceImpl implements GuestbookService {
    private final CommentRepository commentRepository;
    private final HomeRepository homeRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void addComment(CommentRequest commentRequest, String username) {
        Home home=homeRepository.findById(commentRequest.homeId())
                .orElseThrow(RuntimeException::new); //HomeNotFoundException으로 수정하기
        Member member=memberRepository.findByEmail(username)
                .orElseThrow(RuntimeException::new); //UserNotFoundExceptoin으로 수정하기
        Comment comment=Comment.create(commentRequest.comment(), home, member);
        Comment save = commentRepository.save(comment);
        if(!Objects.equals(save.getComment(), commentRequest.comment())){
            throw new DBError("댓글 저장 실패");
        }
    }
}
