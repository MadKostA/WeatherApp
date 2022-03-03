package com.example.WeatherApp.service.impl;

import com.example.WeatherApp.dto.WeatherDto;
import com.example.WeatherApp.dto.YandexWeatherDto.MainDto;
import com.example.WeatherApp.dto.YandexWeatherDto.OpenWeatherApiResponseDto;
import com.example.WeatherApp.service.ExternalWeatherService;
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
public class OpenWeatherApiServiceImpl implements ExternalWeatherService {

    private static final String OPENWEATHERAPI_URL = "http://api.openweathermap.org/data/2.5/weather?q={city}&appid={key}&units=metric";

    @Value("${api.openweatherapi.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public OpenWeatherApiServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public WeatherDto getWeatherByCityAndDate(String city, LocalDate date) {
        URI url = new UriTemplate(OPENWEATHERAPI_URL).expand(city, apiKey);
        ResponseEntity<OpenWeatherApiResponseDto> response;
        try {
            response = restTemplate.getForEntity(url, OpenWeatherApiResponseDto.class);
        } catch (HttpClientErrorException.NotFound exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found", exception);
        } catch (HttpClientErrorException.BadRequest exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request", exception);
        }

        MainDto mainDto = null;
        if (response.getBody() != null)
            mainDto = response.getBody().getMain();
        WeatherDto weather = null;
        if (mainDto != null) {
            weather = new WeatherDto(
                    date,
                    city,
                    response.getBody().getWeatherDtoList().get(0).getMain(),
                    mainDto.getAvgTemp()
            );
        }
        return weather;
    }
}
