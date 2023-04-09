package com.steelman.iot.platform.weather.config;

import com.steelman.iot.platform.weather.webxml.WeatherWebService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WeatherClientConfig {
    @Bean
    public WeatherWebService  weatherWebService(){
        return new WeatherWebService();
    }

    @Bean
    public WeatherUtils  weatherUtils(){
        return  new WeatherUtils(weatherWebService().getWeatherWebServiceSoap());
    }


}
