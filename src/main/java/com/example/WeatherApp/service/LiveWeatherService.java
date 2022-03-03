package com.example.WeatherApp.service;

import com.example.WeatherApp.dto.WeatherDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Qualifier("mainService")
public class LiveWeatherService implements ExternalWeatherService {

    private ExternalWeatherService weatherApiService;
    private ExternalWeatherService openWeatherApiService;

    public LiveWeatherService(@Qualifier("weatherApiService") ExternalWeatherService weatherApiService, @Qualifier("openWeatherApiService") ExternalWeatherService openWeatherApiService) {
        this.weatherApiService = weatherApiService;
        this.openWeatherApiService = openWeatherApiService;
    }

    @Override
    public WeatherDto getWeatherByCityAndDate(String city, LocalDate date) {
//        if (date)
//            return weatherApiService.getWeatherByCityAndDate(city, date);
//        else
        return openWeatherApiService.getWeatherByCityAndDate(city, date);
    }

}