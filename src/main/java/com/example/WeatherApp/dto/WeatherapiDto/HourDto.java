package com.example.WeatherApp.dto.WeatherapiDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class HourDto {

//    @JsonProperty("time_epoch")
//    private JsonNode timeEpoch;
    @JsonProperty("temp_c")
    private Double temp;

}
