package com.example.WeatherApp.dto.YandexWeatherDto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WeatherDescriptionDto {

    @JsonProperty("main")
    private String main;

}
