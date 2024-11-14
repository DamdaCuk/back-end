package com.cuk.damda.guestbook.controller.request;

public record UpdateCommentRequest(
        Long commentId,
        String comment
) {
}
