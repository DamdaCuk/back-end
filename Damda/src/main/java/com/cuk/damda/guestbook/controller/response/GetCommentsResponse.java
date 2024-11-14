package com.cuk.damda.guestbook.controller.response;

public record GetCommentsResponse(
        Long commentId,
        String comment,
        Long authorMemberId
) {
    public static GetCommentsResponse of(Long commentId, String comment, Long authorMemberId) {
        return new GetCommentsResponse(commentId, comment, authorMemberId);
    }
}
