package com.example.WeatherApp.controller;

import com.example.WeatherApp.entities.Weather;
import com.example.WeatherApp.service.LiveWeatherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WeatherController {

    private LiveWeatherService liveWeatherService;

    public WeatherController(LiveWeatherService liveWeatherService) {
        this.liveWeatherService = liveWeatherService;
    }

    @GetMapping(value = "/weather")
    public String getCurrentWeather(@RequestParam String city, @RequestParam String date, Model model) {
        Weather weather = liveWeatherService.getWeatherByCityAndDate(city, date);
        model.addAttribute("weather", weather);
        return "currentWeather";
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