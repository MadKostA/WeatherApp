package com.example.WeatherApp.service.impl;

import com.example.WeatherApp.dto.WeatherapiDto.ForecastDto;
import com.example.WeatherApp.dto.WeatherapiDto.ForecastdayDto;
import com.example.WeatherApp.dto.WeatherapiDto.HourDto;
import com.example.WeatherApp.dto.WeatherapiDto.WeatherResponseDto;
import com.example.WeatherApp.entities.Weather;
import com.example.WeatherApp.repository.WeatherRepo;
import com.example.WeatherApp.service.ExternalWeatherService;
import com.example.WeatherApp.service.prop.WeatherApiUrlServiceProp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
@Slf4j
public class WeatherApiServiceImpl implements ExternalWeatherService {

    @Value("${api.weatherapi.key}")
    private String apiKey;

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
        URI url = new UriTemplate(weatherApiUrlServiceProp.getUrlApi()).expand(apiKey, city, date);
        ResponseEntity<WeatherResponseDto> response;
        try {
            response = restTemplate.getForEntity(url, WeatherResponseDto.class);
            log.debug("Get response {}", response.getBody().toString());
        } catch (HttpClientErrorException.NotFound exception) {
            log.error("Catch not found exception");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found", exception);
        } catch (HttpClientErrorException.BadRequest exception) {
            log.error("Catch bad request exception");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request", exception);
        }
        ForecastDto forecastDto = null;
        if (response.getBody() != null)
            forecastDto = response.getBody().getForecast();
        Weather weather = null;
        if (forecastDto != null) {
            weather = convertCurrentWeather(forecastDto, city, date);
        }
        if (weather != null) {
            weatherRepo.save(weather);
            log.debug("Saved entity in DB {}", weather.toString());
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
