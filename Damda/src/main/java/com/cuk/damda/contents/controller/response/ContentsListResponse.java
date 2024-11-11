package com.cuk.damda.contents.controller.response;

public record ContentsListResponse(
        Long itemId,
        String itemTitle,
        String itemImg
) {
    public static ContentsListResponse from(Long itemId, String itemTitle, String itemImg) {
        return new ContentsListResponse(
                itemId,
                itemTitle,
                itemImg
        );
    }
}
