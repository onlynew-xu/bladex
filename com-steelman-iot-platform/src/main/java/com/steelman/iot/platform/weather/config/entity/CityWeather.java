package com.steelman.iot.platform.weather.config.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityWeather implements Serializable {
    private String citeName;
    private String temperature;
    private String humidity;
    private String info;
    private String imgOne;
    private String imgTwo;
    private String time;

}
