package com.example.WeatherApp.service;

import com.example.WeatherApp.entities.Weather;
import com.example.WeatherApp.repository.WeatherRepo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
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

    @Autowired
    private WeatherRepo weatherRepo;

    public LiveWeatherService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Transactional
    void saveWeatherToDB(Weather weather) {
        weatherRepo.save(weather);
    }

    public Weather getWeatherByCityAndDate(String city, String date) {
        URI url = new UriTemplate(WEATHER_URL).expand(apiKey, city, date);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        Weather weather = convertCurrentWeather(response, city, date);
        saveWeatherToDB(weather);
        return weather;
    }

    private Weather convertCurrentWeather(ResponseEntity<String> response, String city, String date) {
        JSONObject jo;
        try {
            jo = (JSONObject) new JSONParser().parse(response.getBody());
        } catch (ParseException e) {
            throw new RuntimeException("Error parsing JSON", e);
        }
        JSONObject forecast = (JSONObject) jo.get("forecast");
        JSONArray forecastday = (JSONArray) forecast.get("forecastday");
        JSONObject innerObject = (JSONObject) forecastday.get(0);
        JSONObject day = (JSONObject) innerObject.get("day");
        MathContext context = new MathContext(3, RoundingMode.DOWN);
        BigDecimal value = new BigDecimal(calculateAverage((JSONArray) innerObject.get("hour")), context);
        return new Weather(
                date,
                city,
                (String) ((JSONObject) day.get("condition")).get("text"),
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