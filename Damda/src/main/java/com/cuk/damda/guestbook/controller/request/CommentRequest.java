package com.cuk.damda.guestbook.controller.request;

public record CommentRequest(
        String comment,
        Long homeId
) {
}
