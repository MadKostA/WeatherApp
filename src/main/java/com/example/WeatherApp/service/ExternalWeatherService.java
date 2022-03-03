package com.example.WeatherApp.service;

import com.example.WeatherApp.dto.WeatherDto;

import java.time.LocalDate;

public interface ExternalWeatherService {
//    Weather getWeatherByCityAndDate();
    WeatherDto getWeatherByCityAndDate(String city, LocalDate date);
}
