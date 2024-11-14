package com.cuk.damda.home.service;

import com.cuk.damda.home.controller.response.HomeResponse;
import com.cuk.damda.home.domain.Home;

import java.util.List;

public interface HomeService {
    void memberInsertHome(Home home, String userName);
    Home createHome(String homeName);
    List<HomeResponse> searchHome(String title, String contentType);
}
