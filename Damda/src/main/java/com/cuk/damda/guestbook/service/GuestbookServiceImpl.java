package com.cuk.damda.guestbook.service;

import com.cuk.damda.global.exception.exceptions.DBError;
import com.cuk.damda.guestbook.controller.request.CommentRequest;
import com.cuk.damda.guestbook.controller.request.DeleteCommentRequest;
import com.cuk.damda.guestbook.controller.request.UpdateCommentRequest;
import com.cuk.damda.guestbook.controller.response.GetCommentsResponse;
import com.cuk.damda.guestbook.domain.Comment;
import com.cuk.damda.guestbook.repository.CommentRepository;
import com.cuk.damda.home.domain.Home;
import com.cuk.damda.home.repository.HomeRepository;
import com.cuk.damda.member.domain.Member;
import com.cuk.damda.member.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
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
    private final EntityManager em;

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

    @Override
    @Transactional(readOnly = true)
    public List<GetCommentsResponse> getComments(Long homeId) {
        Home home=homeRepository.findById(homeId)
                .orElseThrow(RuntimeException::new); //나중에 HomeNotFound로 변경

        List<Comment>comments= commentRepository.findByHome(home);
        List<GetCommentsResponse>result=new ArrayList<>();

        for(Comment comment:comments){
            GetCommentsResponse commentResponse=GetCommentsResponse.of(comment.getCommentId(), comment.getComment(), comment.getAuthor().getUserId());
            result.add(commentResponse);
        }
        return result;
    }

    @Override
    public void updateComment(UpdateCommentRequest updateCommentRequest, String userEmail) {
        Comment comment=em.find(Comment.class,updateCommentRequest.commentId());
        if(comment.getAuthor().getEmail().equals(userEmail)){ //작성자가 로그인 한 유저라면
            comment.updateComment(updateCommentRequest.comment());
        }else{
            throw new RuntimeException("로그인 한 유저와 댓글을 작성한 유저가 다릅니다.");
        }
        em.close();
    }

    @Override
    @Transactional()
    public void deleteComment(DeleteCommentRequest deleteCommentRequest, String username) {
        Comment comment = commentRepository.findById(deleteCommentRequest.commentId())
                .orElseThrow(() -> new IllegalArgumentException("작성된 댓글이 존재하지 않습니다."));

        if(comment.getAuthor().getEmail().equals(username)){
            commentRepository.delete(comment);
        }else{
            throw new RuntimeException("로그인 한 유저와 댓글을 작성한 유저가 다릅니다.");
        }
        em.close();
    }
}
