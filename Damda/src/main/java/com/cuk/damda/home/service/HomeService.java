package com.cuk.damda.home.service;

import com.cuk.damda.home.controller.response.HomeResponse;
import com.cuk.damda.home.domain.Home;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HomeService {
    void memberInsertHome(Home home, String userName);
    Home createHome(String homeName);
    Page<HomeResponse> searchHomes(String title, String contentType, Pageable pageable);
}
