package com.example.WeatherApp.dto.WeatherapiDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WeatherResponseDto {

    @JsonProperty("forecast")
    private ForecastDto forecast;

}
