package com.example.WeatherApp.service.prop;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.util.UriComponentsBuilder;

@ConfigurationProperties(prefix = "openweatherapi")
@Getter
@Setter
public class OpenWeatherApiUrlServiceProp {

    private String scheme;
    private String host;
    private String path;

    public String getUrlApi() {
        return UriComponentsBuilder.newInstance()
                .scheme(scheme)
                .host(host)
                .path(path)
                .build().toUriString();
    }

}
