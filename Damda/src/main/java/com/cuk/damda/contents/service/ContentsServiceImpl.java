package com.cuk.damda.contents.service;

import com.cuk.damda.contents.controller.response.ContentsListResponse;
import com.cuk.damda.contents.domain.Contents;
import com.cuk.damda.contents.domain.Enum.ItemType;
import com.cuk.damda.contents.repository.ContentsRepository;
import com.cuk.damda.home.domain.Home;
import com.cuk.damda.home.repository.HomeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class ContentsServiceImpl implements ContentsService {
    private final HomeRepository homeRepository;
    private final ContentsRepository contentsRepository;

    @Override
    public Slice<ContentsListResponse> getContentsList(int page, int size, Long homeId, ItemType itemType) {
        log.info("ItemType: " + itemType);

        Home testHome = homeRepository.findByHomeId(homeId)
                .orElseThrow(() -> new IllegalArgumentException("home을 찾을 수 없습니다."));

        Pageable pageable = PageRequest.of(page, size);
        Slice<Contents> contentsList = contentsRepository.findByHomeAndItemType(pageable, testHome, itemType);

        Slice<ContentsListResponse> contentsListResponses = contentsList.map(contents ->
                    ContentsListResponse.from(
                        contents.getItemId(),
                        contents.getItemTitle(),
                        contents.getItemImg()
                    )
                );

        return contentsListResponses;
    }
}
