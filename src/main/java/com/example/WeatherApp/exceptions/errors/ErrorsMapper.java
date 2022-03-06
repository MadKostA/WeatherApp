package com.example.WeatherApp.exceptions.errors;

import com.example.WeatherApp.dto.exceptionsDto.ErrorDto;
import lombok.experimental.UtilityClass;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ErrorsMapper {

    public static List<ErrorDto> getErrorsList(BindingResult result){
        List<ErrorDto> errorDtoList = new ArrayList<>();
        result.getAllErrors().forEach(error -> {
            errorDtoList.add(new ErrorDto("ERROR", error.getDefaultMessage()));
        });
        return errorDtoList;
    }

}
