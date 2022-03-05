package com.example.WeatherApp.dto;

import lombok.Data;

@Data
public class CityDto {

    private String name;
    private String region;
    private Double lat;
    private Double lon;

}
