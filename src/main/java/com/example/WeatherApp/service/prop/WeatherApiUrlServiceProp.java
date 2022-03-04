package com.example.WeatherApp.service.prop;


import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;

@ConfigurationProperties(prefix = "weatherapi")
@Getter
@Setter
public class WeatherApiUrlServiceProp {

    @Value("${api.weatherapi.key}")
    private String key;
    private String scheme;
    private String host;
    private String path;

    public String getUrlApi(String city, LocalDate date) {
        return UriComponentsBuilder.newInstance()
                .scheme(scheme)
                .host(host)
                .path(path)
                .queryParam("key", key)
                .queryParam("q", city)
                .queryParam("dt", date)
                .build().toUriString();
    }

}