package com.example.WeatherApp.dto.YandexWeatherDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class OpenWeatherApiResponseDto {

    @JsonProperty("main")
    private MainDto main;
    @JsonProperty("weather")
    private List<WeatherDescriptionDto> weatherDtoList;

}
