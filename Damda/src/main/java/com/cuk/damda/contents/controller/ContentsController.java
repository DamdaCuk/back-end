package com.cuk.damda.contents.controller;

import com.cuk.damda.contents.controller.response.ContentsListResponse;
import com.cuk.damda.contents.domain.Enum.ItemType;
import com.cuk.damda.contents.service.ContentsService;
import com.cuk.damda.global.controller.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/contents")
public class ContentsController {

    private final ContentsService contentsService;

    @GetMapping("/{itemType}/{homeId}/list")
    public ApiResponse<Slice<ContentsListResponse>> getContentsList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @PathVariable Long homeId,
            @PathVariable String itemType){
        ItemType type = ItemType.valueOf(itemType.toUpperCase());
        return ApiResponse.ok(contentsService.getContentsList(page, size, homeId, type));
    }
}
