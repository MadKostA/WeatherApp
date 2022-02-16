package com.example.WeatherApp.repository;

import com.example.WeatherApp.entities.Weather;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherRepo extends CrudRepository<Weather, Long> {
//    @Modifying
//    @Query(
//            value =
//                    "insert into weather (avg_temp, city, date, description) values (:avg_temp, :city, :date, :description)",
//            nativeQuery = true)
//    void insertWeather(@Param("avg_temp") Double avg_temp, @Param("city") String city,
//                       @Param("date") String date, @Param("description") String description);

}
