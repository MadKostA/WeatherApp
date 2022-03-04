package com.example.WeatherApp.service;

import com.example.WeatherApp.entities.Weather;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LiveWeatherService {

    private final List<ExternalWeatherService> externalWeatherServiceList;

    public LiveWeatherService(List<ExternalWeatherService> externalWeatherServiceList) {
        this.externalWeatherServiceList = externalWeatherServiceList;
    }

    public Weather getWeatherByCityAndDate(String city, LocalDate date){
        Weather weatherDto = null;
        for (ExternalWeatherService service : externalWeatherServiceList){
            weatherDto = service.getWeatherByCityAndDate(city, date);
            if (weatherDto != null)
                break;
        }
        return weatherDto;
    }

}