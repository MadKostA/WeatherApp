package com.example.WeatherApp.service;

import com.example.WeatherApp.dto.CityDto;

import java.util.List;

public interface CityCrudService {

    CityDto getByName(String name);
    void save(CityDto cityDto);
    CityDto update(String name, CityDto cityDto);
    void delete(String name);
    List<CityDto> getAll();

    boolean checkCity(String name);
}
