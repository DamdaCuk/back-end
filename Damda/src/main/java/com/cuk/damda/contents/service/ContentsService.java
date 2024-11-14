package com.cuk.damda.contents.service;

import com.cuk.damda.contents.controller.response.ContentsListResponse;
import com.cuk.damda.contents.domain.Enum.ItemType;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ContentsService {
    Slice<ContentsListResponse> getContentsList(int page, int size, Long homeId, ItemType itemType);
    void deleteContents(Long contentsId);
}
