package com.example.WeatherApp.dto.WeatherapiDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ForecastdayDto {

    @JsonProperty("date")
    private String date;
    @JsonProperty("hour")
    private List<HourDto> hour;

}
