package com.example.WeatherApp.controller;

import com.example.WeatherApp.entities.Weather;
import com.example.WeatherApp.service.LiveWeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@Slf4j
public class WeatherController {

    private LiveWeatherService weatherService;

    public WeatherController(LiveWeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping(value = "/weather")
    public ResponseEntity<Weather> getCurrentWeather(
            @RequestParam String city,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date)
    {
        log.info("Request with params city={}, date={}", city, date);
        Weather weather = weatherService.getWeatherByCityAndDate(city, date);
        return new ResponseEntity<>(weather, HttpStatus.OK);
    }
}