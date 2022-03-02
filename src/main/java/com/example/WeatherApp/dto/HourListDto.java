package com.example.WeatherApp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.boot.jackson.JsonComponent;

import java.util.List;

@Data
public class HourListDto {

    @JsonProperty("hour")
    private List<HourDto> hourDtoList;

}
