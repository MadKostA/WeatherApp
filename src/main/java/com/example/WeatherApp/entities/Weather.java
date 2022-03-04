package com.example.WeatherApp.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table
@Setter
@Getter
@NoArgsConstructor
public class Weather implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDate date;
    private String city;
    private String description;
    private Double avgTemp;

    public Weather(LocalDate date, String city, String description, Double avgTemp) {
        this.date = date;
        this.city = city;
        this.description = description;
        this.avgTemp = avgTemp;
    }
}

