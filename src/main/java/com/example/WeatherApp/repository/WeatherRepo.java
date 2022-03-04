package com.example.WeatherApp.repository;

import com.example.WeatherApp.entities.Weather;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherRepo extends CrudRepository<Weather, Long> {

}
