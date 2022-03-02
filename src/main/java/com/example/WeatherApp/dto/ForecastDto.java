package com.example.WeatherApp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ForecastDto {

    @JsonProperty("forecastday")
    private List<ForecastdayDto> forecastday;
}
