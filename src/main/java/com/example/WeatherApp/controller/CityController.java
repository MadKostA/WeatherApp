package com.example.WeatherApp.controller;

import com.example.WeatherApp.dto.CityDto;
import com.example.WeatherApp.dto.exceptionsDto.ErrorDto;
import com.example.WeatherApp.exceptions.errors.ErrorsMapper;
import com.example.WeatherApp.service.CityCrudService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(value = "/city")
@Validated
public class CityController {

    private final CityCrudService cityService;

    public CityController(CityCrudService cityService) {
        this.cityService = cityService;
    }

    @GetMapping(value = "{name}")
    public ResponseEntity<CityDto> getCityByName(@PathVariable("name") String name) {
        CityDto city = this.cityService.getByName(name);
        if (city != null)
            return new ResponseEntity<>(city, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CityDto>> getAllCities() {
        List<CityDto> list = this.cityService.getAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> saveCity(@Valid @RequestBody CityDto city, BindingResult bindingResult) {
        log.info("Request for save {}", city);
        if (bindingResult.hasErrors()) {
            List<ErrorDto> errorsList = ErrorsMapper.getErrorsList(bindingResult);
            log.info("Errors in input dto {}", errorsList);
            return ResponseEntity.badRequest().body(errorsList);
        }

        Optional<CityDto> response = Optional.ofNullable(cityService.save(city));

        return response.isPresent()
                ? new ResponseEntity<>(response.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        final CityDto cityDto = cityService.save(city);
//        URI location = ServletUriComponentsBuilder
//                .fromCurrentRequest()
//                .path("/{name}")
//                .buildAndExpand(city.getName())
//                .toUri();
//        return ResponseEntity.created(location).body(cityDto);
    }

    @PutMapping(value = "{name}")
    public ResponseEntity<?> updateCity(@PathVariable("name") String name,
                                        @RequestBody @Valid CityDto city, BindingResult bindingResult) {
        log.info("Request for update {}", name);
        if (bindingResult.hasErrors()) {
            List<ErrorDto> errorsList = ErrorsMapper.getErrorsList(bindingResult);
            log.info("Errors in input dto {}", errorsList);
            return ResponseEntity.badRequest().body(errorsList);
        }
        if (!cityService.checkCity(name)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (city == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        this.cityService.save(city);
        return new ResponseEntity<>(city, HttpStatus.OK);
    }

    @DeleteMapping(value = "{name}")
    public ResponseEntity<CityDto> deleteCity(@PathVariable("name") String name) {
        this.cityService.delete(name);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
