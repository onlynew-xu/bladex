package com.steelman.iot.platform.weather.config;

import com.steelman.iot.platform.weather.webxml.ArrayOfString;
import com.steelman.iot.platform.weather.webxml.WeatherWebService;
import com.steelman.iot.platform.weather.webxml.WeatherWebServiceSoap;

public class WeatherTest {
    public static void main(String[] args) {
        WeatherWebServiceSoap weatherWebServiceSoap=new WeatherWebService().getWeatherWebServiceSoap();
        ArrayOfString henan = weatherWebServiceSoap.getSupportCity("河南");
        ArrayOfString test = weatherWebServiceSoap.getWeatherbyCityName("商丘");
        System.out.println(123);
    }
}
