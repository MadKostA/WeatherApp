package com.example.WeatherApp.service.impl;

import com.example.WeatherApp.dto.WeatherDto;
import com.example.WeatherApp.dto.WeatherapiDto.ForecastDto;
import com.example.WeatherApp.dto.WeatherapiDto.ForecastdayDto;
import com.example.WeatherApp.dto.WeatherapiDto.HourDto;
import com.example.WeatherApp.dto.WeatherapiDto.WeatherResponseDto;
import com.example.WeatherApp.repository.WeatherRepo;
import com.example.WeatherApp.service.ExternalWeatherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriTemplate;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URI;
import java.time.LocalDate;

@Service
@Qualifier("weatherApiService")
public class WeatherApiServiceImpl implements ExternalWeatherService {
    private static final String WEATHERAPI_URL = "http://api.weatherapi.com/v1/history.json?key={key}&q={city}&dt={date}";

    @Value("${api.weatherapi.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public WeatherApiServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public WeatherDto getWeatherByCityAndDate(String city, LocalDate date) {
        URI url = new UriTemplate(WEATHERAPI_URL).expand(apiKey, city, date);
        ResponseEntity<WeatherResponseDto> response;
        try {
            response = restTemplate.getForEntity(url, WeatherResponseDto.class);
        } catch (HttpClientErrorException.NotFound exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found", exception);
        } catch (HttpClientErrorException.BadRequest exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request", exception);
        }
        ForecastDto forecastDto = null;
        if (response.getBody() != null)
            forecastDto = response.getBody().getForecast();
        WeatherDto weather = null;
        if (forecastDto != null) {
            weather = convertCurrentWeather(forecastDto, city, date);
        }
        return weather;
    }

    private WeatherDto convertCurrentWeather(ForecastDto forecastDto, String city, LocalDate date) {
        ForecastdayDto forecastdayDto = forecastDto.getForecastday().get(0);
        double average = forecastdayDto
                .getHour()
                .stream()
                .map(HourDto::getTemp)
                .reduce(0.0, Double::sum) / 24;
        MathContext context = new MathContext(3, RoundingMode.DOWN);
        BigDecimal value = new BigDecimal(average, context);
        return new WeatherDto(
                date,
                city,
                "some text",
                value.doubleValue()
        );
    }
}
