package com.example.WeatherApp.dto.exceptionsDto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ErrorDto {
    private String code;
    private String message;
}
