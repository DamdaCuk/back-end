package com.cuk.damda.home.service;

import com.cuk.damda.home.domain.Home;

public interface HomeService {
    void memberInsertHome(Home home, String userName);
    Home createHome(String homeName);
}
