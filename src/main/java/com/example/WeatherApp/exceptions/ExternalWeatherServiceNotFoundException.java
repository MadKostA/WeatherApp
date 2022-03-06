package com.example.WeatherApp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ExternalWeatherServiceNotFoundException extends RuntimeException {
    public ExternalWeatherServiceNotFoundException(String message) {
        super(message);
    }
}
