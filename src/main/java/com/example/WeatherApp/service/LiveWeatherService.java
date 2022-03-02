package com.example.WeatherApp.service;

import com.example.WeatherApp.dto.ForecastDto;
import com.example.WeatherApp.dto.ForecastdayDto;
import com.example.WeatherApp.dto.WeatherResponseDto;
import com.example.WeatherApp.entities.Weather;
import com.example.WeatherApp.repository.WeatherRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import javassist.tools.web.BadHttpRequest;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriTemplate;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URI;

@Service
public class LiveWeatherService {

    private static final String WEATHER_URL = "http://api.weatherapi.com/v1/history.json?key={key}&q={city}&dt={date}";

    @Value("${api.weatherapi.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private WeatherRepo weatherRepo;
    private ObjectMapper objectMapper;

    public LiveWeatherService(RestTemplateBuilder restTemplateBuilder, WeatherRepo weatherRepo, ObjectMapper objectMapper) {
        this.restTemplate = restTemplateBuilder.build();
        this.weatherRepo = weatherRepo;
        this.objectMapper = objectMapper;
    }

    @Transactional
    void saveWeatherToDB(Weather weather) {
        weatherRepo.save(weather);
    }

    public Weather getWeatherByCityAndDate(String city, String date) {
        URI url = new UriTemplate(WEATHER_URL).expand(apiKey, city, date);
        ResponseEntity<WeatherResponseDto> response = null;
        try {
            response = restTemplate.getForEntity(url, WeatherResponseDto.class);
        } catch (HttpClientErrorException.NotFound exception){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found", exception);
        } catch (HttpClientErrorException.BadRequest exception){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request", exception);
        }

        ForecastDto forecastDto = response.getBody().getForecast();
        Weather weather = convertCurrentWeather(forecastDto, city, date);
        saveWeatherToDB(weather);
        return weather;
    }

    private Weather convertCurrentWeather(ForecastDto forecastDto, String city, String date) {
        ForecastdayDto forecastdayDto = forecastDto.getForecastday().get(0);
        Double average = forecastdayDto.getHour().stream().map(x -> x.getTemp()).reduce(0.0, Double::sum).doubleValue()/24;
        MathContext context = new MathContext(3, RoundingMode.DOWN);
        BigDecimal value = new BigDecimal(average, context);
        return new Weather(
                forecastdayDto.getDate(),
                city,
            "some text",
                value.doubleValue()
        );
    }

    private Double calculateAverage(JSONArray hourArr) {
        Double sum = 0.0;
        for (Object jo : hourArr) {
            JSONObject jsonObject = (JSONObject) jo;
            sum += (Double) jsonObject.get("temp_c");
        }
        return sum / 24;
    }
}