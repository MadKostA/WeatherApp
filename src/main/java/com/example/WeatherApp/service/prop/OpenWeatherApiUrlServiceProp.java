package com.example.WeatherApp.service.prop;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.util.UriComponentsBuilder;

@ConfigurationProperties(prefix = "openweatherapi")
@Getter
@Setter
public class OpenWeatherApiUrlServiceProp {

    @Value("${api.openweatherapi.key}")
    private String key;
    private String scheme;
    private String host;
    private String path;

    public String getUrlApi(String city) {
        return UriComponentsBuilder.newInstance()
                .scheme(scheme)
                .host(host)
                .path(path)
                .queryParam("q",city)
                .queryParam("appid", key)
                .queryParam("units", "metric")
                .build().toUriString();
    }

}
