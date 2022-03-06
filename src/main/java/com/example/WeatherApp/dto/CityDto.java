package com.example.WeatherApp.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class CityDto {

    @NotEmpty(message = "Name cannot be null")
    private String name;

    @NotEmpty(message = "Region cannot be null")
    private String region;

    @Size(min = -90, max = 90)
    private Double lat;

    @Size(min = -180, max = 180)
    private Double lon;

}
