package com.example.WeatherApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class WeatherDto {

    private LocalDate date;
    private String city;
    private String description;
    private Double avgTemp;

}
