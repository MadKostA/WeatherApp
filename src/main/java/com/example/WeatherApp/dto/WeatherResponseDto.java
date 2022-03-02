package com.example.WeatherApp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import org.springframework.boot.jackson.JsonComponent;

@Data
public class WeatherResponseDto {

    @JsonProperty("forecast")
    private ForecastDto forecast;

}
