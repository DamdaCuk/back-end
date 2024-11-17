package com.cuk.damda.home.service;

import com.cuk.damda.home.controller.response.HomeListResponse;
import com.cuk.damda.home.domain.Home;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HomeService {
    void memberInsertHome(Home home, String userName);
    Home createHome(String homeName);
    Page<HomeListResponse> searchHomes(Long itemId, String contentType, Pageable pageable);
}
