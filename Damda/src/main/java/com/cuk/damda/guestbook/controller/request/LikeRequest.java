package com.cuk.damda.guestbook.controller.request;

import lombok.Getter;

@Getter
public record LikeRequest (
        Long homeId
){

}
