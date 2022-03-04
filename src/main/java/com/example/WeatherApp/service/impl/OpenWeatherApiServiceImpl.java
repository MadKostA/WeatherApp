package com.example.WeatherApp.service.impl;

import com.example.WeatherApp.dto.WeatherDto;
import com.example.WeatherApp.dto.YandexWeatherDto.MainDto;
import com.example.WeatherApp.dto.YandexWeatherDto.OpenWeatherApiResponseDto;
import com.example.WeatherApp.entities.Weather;
import com.example.WeatherApp.repository.WeatherRepo;
import com.example.WeatherApp.service.ExternalWeatherService;
import com.example.WeatherApp.service.prop.OpenWeatherApiUrlServiceProp;
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

import java.net.URI;
import java.time.LocalDate;

@Service
@Qualifier("openWeatherApiService")
@Slf4j
public class OpenWeatherApiServiceImpl implements ExternalWeatherService {

    @Value("${api.openweatherapi.key}")
    private String apiKey;

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
        URI url = new UriTemplate(openWeatherApiUrlServiceProp.getUrlApi()).expand(city, apiKey);
        ResponseEntity<OpenWeatherApiResponseDto> response;
        try {
            response = restTemplate.getForEntity(url, OpenWeatherApiResponseDto.class);
            log.debug("Get response {}", response.getBody().toString());
        } catch (HttpClientErrorException.NotFound exception) {
            log.error("Catch not found exception");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found", exception);
        } catch (HttpClientErrorException.BadRequest exception) {
            log.error("Catch bad request exception");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request", exception);
        }

        MainDto mainDto = null;
        if (response.getBody() != null)
            mainDto = response.getBody().getMain();
        Weather weather = null;
        if (mainDto != null) {
            weather = new Weather(
                    date,
                    city,
                    response.getBody().getWeatherDtoList().get(0).getMain(),
                    mainDto.getAvgTemp()
            );
        }
        if (weather != null) {
            weatherRepo.save(weather);
            log.debug("Saved entity in DB {}", weather.toString());
        }
        return weather;
    }
}
