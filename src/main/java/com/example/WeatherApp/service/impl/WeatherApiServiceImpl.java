package com.example.WeatherApp.service.impl;

import com.example.WeatherApp.dto.WeatherapiDto.ForecastDto;
import com.example.WeatherApp.dto.WeatherapiDto.ForecastdayDto;
import com.example.WeatherApp.dto.WeatherapiDto.HourDto;
import com.example.WeatherApp.dto.WeatherapiDto.WeatherResponseDto;
import com.example.WeatherApp.entities.Weather;
import com.example.WeatherApp.exceptions.BadRequestException;
import com.example.WeatherApp.exceptions.NotFoundException;
import com.example.WeatherApp.repository.WeatherRepo;
import com.example.WeatherApp.service.ExternalWeatherService;
import com.example.WeatherApp.service.prop.WeatherApiUrlServiceProp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
@Qualifier("weatherApiService")
@Slf4j
public class WeatherApiServiceImpl implements ExternalWeatherService {

    private final RestTemplate restTemplate;
    private final WeatherApiUrlServiceProp weatherApiUrlServiceProp;
    private final WeatherRepo weatherRepo;

    public WeatherApiServiceImpl(RestTemplate restTemplate, WeatherApiUrlServiceProp weatherApiUrlServiceProp, WeatherRepo weatherRepo) {
        this.restTemplate = restTemplate;
        this.weatherApiUrlServiceProp = weatherApiUrlServiceProp;
        this.weatherRepo = weatherRepo;
    }

    @Override
    public Weather getWeatherByCityAndDate(String city, LocalDate date) {
        String url = weatherApiUrlServiceProp.getUrlApi(city, date);
        ResponseEntity<WeatherResponseDto> response;
        try {
            response = restTemplate.getForEntity(url, WeatherResponseDto.class);
        } catch (HttpClientErrorException.NotFound exception) {
            log.error("Catch not found exception");
            throw new NotFoundException("Not found");
        } catch (HttpClientErrorException.BadRequest exception) {
            log.error("Catch bad request exception");
            throw new BadRequestException("Bad request");
        }
        ForecastDto forecastDto = null;
        if (response.getBody() != null) {
            log.debug("Get response {}", response.getBody());
            forecastDto = response.getBody().getForecast();
        }
        Weather weather = null;
        if (forecastDto != null) {
            weather = convertCurrentWeather(forecastDto, city, date);
            log.debug("Calculated average and created entity");
        }
        if (weather != null) {
            weatherRepo.save(weather);
            log.debug("Saved entity in DB {}", weather);
        }
        return weather;
    }

    private Weather convertCurrentWeather(ForecastDto forecastDto, String city, LocalDate date) {
        ForecastdayDto forecastdayDto = forecastDto.getForecastday().get(0);
        double average = forecastdayDto
                .getHour()
                .stream()
                .map(HourDto::getTemp)
                .reduce(0.0, Double::sum) / 24;
        MathContext context = new MathContext(3, RoundingMode.DOWN);
        BigDecimal value = new BigDecimal(average, context);
        return new Weather(
                date,
                city,
                "some text",
                value.doubleValue()
        );
    }
}
