package com.example.WeatherApp.dto.OpenWeatherApiDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MainDto {

    @JsonProperty("temp")
    private Double avgTemp;

}
