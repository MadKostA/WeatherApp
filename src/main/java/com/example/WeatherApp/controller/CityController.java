package com.example.WeatherApp.controller;

import com.example.WeatherApp.dto.CityDto;
import com.example.WeatherApp.service.CityCrudService;
import com.example.WeatherApp.service.CityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController()
@RequestMapping(value = "/city")
public class CityController {

    private CityCrudService cityService;

    public CityController(CityCrudService cityService){
        this.cityService = cityService;
    }

    @GetMapping(value = "{name}")
    public ResponseEntity<CityDto> getCityByName(@PathVariable("name") String name){
        CityDto city = this.cityService.getByName(name);
        if (city != null)
            return new ResponseEntity<>(city, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CityDto>> getAllCities(){
        List<CityDto> list = this.cityService.getAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<CityDto> saveCity(@RequestBody @Valid CityDto city){
        if (city == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        this.cityService.save(city);
        return new ResponseEntity<>(city, HttpStatus.CREATED);
    }

    @PutMapping(value = "{name}")
    public ResponseEntity<CityDto> updateCity(@PathVariable("name") String name,
                                              @RequestBody @Valid CityDto city){
        if (!cityService.checkCity(name)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (city == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        this.cityService.save(city);
        return new ResponseEntity<>(city, HttpStatus.OK);
    }

    @DeleteMapping(value = "{name}")
    public ResponseEntity<CityDto> deleteCity(@PathVariable("name") String name){
        this.cityService.delete(name);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
