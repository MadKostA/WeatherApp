package com.example.WeatherApp.service.impl;

import com.example.WeatherApp.dto.OpenWeatherApiDto.MainDto;
import com.example.WeatherApp.dto.OpenWeatherApiDto.OpenWeatherApiResponseDto;
import com.example.WeatherApp.entities.Weather;
import com.example.WeatherApp.exceptions.ExternalWeatherServiceBadRequestException;
import com.example.WeatherApp.exceptions.ExternalWeatherServiceNotFoundException;
import com.example.WeatherApp.repository.WeatherRepo;
import com.example.WeatherApp.service.ExternalWeatherService;
import com.example.WeatherApp.service.prop.OpenWeatherApiUrlServiceProp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Service
@Qualifier("openWeatherApiService")
@Slf4j
public class OpenWeatherApiServiceImpl implements ExternalWeatherService {

    private final RestTemplate restTemplate;
    private final OpenWeatherApiUrlServiceProp openWeatherApiUrlServiceProp;
    private final WeatherRepo weatherRepo;

    public OpenWeatherApiServiceImpl(RestTemplate restTemplate, OpenWeatherApiUrlServiceProp openWeatherApiUrlServiceProp, WeatherRepo weatherRepo) {
        this.restTemplate = restTemplate;
        this.openWeatherApiUrlServiceProp = openWeatherApiUrlServiceProp;
        this.weatherRepo = weatherRepo;
    }

    @Override
    public Weather getWeatherByCityAndDate(String city, LocalDate date) {
        String url = openWeatherApiUrlServiceProp.getUrlApi(city);
        ResponseEntity<OpenWeatherApiResponseDto> response;
        try {
            response = restTemplate.getForEntity(url, OpenWeatherApiResponseDto.class);

        } catch (HttpClientErrorException.NotFound exception) {
            log.error("Catch not found exception");
            throw new ExternalWeatherServiceNotFoundException("Not found from");
        } catch (HttpClientErrorException.BadRequest exception) {
            log.error("Catch bad request exception");
            throw new ExternalWeatherServiceBadRequestException("Bad request from");
        }

        MainDto mainDto = null;
        if (response.getBody() != null) {
            log.debug("Get response {}", response.getBody());
            mainDto = response.getBody().getMain();
        }
        Weather weather = null;
        if (mainDto != null) {
            weather = new Weather(
                    date,
                    city,
                    response.getBody().getWeatherDtoList().get(0).getMain(),
                    mainDto.getAvgTemp()
            );
            log.debug("Created entity");
        }
        if (weather != null) {
            weatherRepo.save(weather);
            log.debug("Saved entity in DB {}", weather);
        }
        return weather;
    }
}
