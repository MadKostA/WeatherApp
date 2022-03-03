package com.example.WeatherApp.dto.WeatherapiDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class HourListDto {

    @JsonProperty("hour")
    private List<HourDto> hourDtoList;

}
