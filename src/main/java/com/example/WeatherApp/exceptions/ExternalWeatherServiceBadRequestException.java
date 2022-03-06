package com.example.WeatherApp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ExternalWeatherServiceBadRequestException extends RuntimeException {
    public ExternalWeatherServiceBadRequestException(String message) {
        super(message);
    }
}
