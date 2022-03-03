package com.example.WeatherApp.controller;

import com.example.WeatherApp.dto.WeatherDto;
import com.example.WeatherApp.service.ExternalWeatherService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class WeatherController {

    private ExternalWeatherService weatherService;

    public WeatherController(@Qualifier("mainService") ExternalWeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping(value = "/weather")
    public ResponseEntity<WeatherDto> getCurrentWeather(@RequestParam String city, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date){
        WeatherDto weather = weatherService.getWeatherByCityAndDate(city, date);
        return new ResponseEntity<>(weather, HttpStatus.OK);
    }
}







//    @GetMapping("/historicalWeather")
//    public String getHistoricalWeather(@RequestParam String start, @RequestParam(required = false) String end, Model model) throws ParseException {
////        if (true) {
//            model.addAttribute("currentWeather", liveWeatherService.getHistoricalWeatherByCityAndDate("Moscow", "RU", start, end));
////        }
////        } else {
////            model.addAttribute("currentWeather", stubWeatherService.getCurrentWeather("Moscow","ru"));
////        }
//        return "currentWeather";
//    }