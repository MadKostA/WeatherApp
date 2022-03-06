package com.example.WeatherApp.controllerAdvice;

import com.example.WeatherApp.exceptions.ExternalWeatherServiceBadRequestException;
import com.example.WeatherApp.exceptions.ExternalWeatherServiceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class DefaultAdvice {

    @ExceptionHandler(ExternalWeatherServiceBadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(RuntimeException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExternalWeatherServiceNotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(RuntimeException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

}
