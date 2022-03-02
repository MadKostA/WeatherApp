package com.example.WeatherApp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.util.List;

@Data
public class ForecastdayDto {

    @JsonProperty("date")
    private String date;
    @JsonProperty("hour")
    private List<HourDto> hour;

}
