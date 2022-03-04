package com.example.WeatherApp.service;

import com.example.WeatherApp.entities.Weather;

import java.time.LocalDate;

public interface ExternalWeatherService {
    Weather getWeatherByCityAndDate(String city, LocalDate date);
}
