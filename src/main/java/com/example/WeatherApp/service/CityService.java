package com.example.WeatherApp.service;

import com.example.WeatherApp.dto.CityDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CityService implements CityCrudService {

    private final Map<String, CityDto> cityDB;

    public CityService() {
        cityDB = new HashMap<>();
    }

    @Override
    public CityDto getByName(String name) {
        log.debug("Get city {}", name);
        return cityDB.get(name);
    }

    @Override
    public CityDto save(CityDto cityDto) {
        log.debug("Saved city {}", cityDto.getName());
        cityDB.put(cityDto.getName(), cityDto);
        return cityDto;
    }

    @Override
    public CityDto update(String name, CityDto cityDto){
        log.debug("Update city {}", cityDto.getName());
        return cityDB.put(cityDto.getName(), cityDto);
    }

    public boolean checkCity(String name){
        return cityDB.containsKey(name);
    }

    @Override
    public void delete(String name) {
        log.debug("Delete city {}", name);
        cityDB.remove(name);
    }

    @Override
    public List<CityDto> getAll() {
        log.debug("Get all cities");
        return new ArrayList<>(cityDB.values());
    }


}
