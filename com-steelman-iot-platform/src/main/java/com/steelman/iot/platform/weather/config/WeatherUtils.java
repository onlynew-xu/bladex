package com.steelman.iot.platform.weather.config;

import com.steelman.iot.platform.weather.config.entity.CityWeather;
import com.steelman.iot.platform.weather.webxml.ArrayOfString;
import com.steelman.iot.platform.weather.webxml.WeatherWebServiceSoap;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class WeatherUtils {

    private WeatherWebServiceSoap  weatherWebServiceSoap;

    public WeatherUtils(WeatherWebServiceSoap weatherWebServiceSoap) {
        this.weatherWebServiceSoap = weatherWebServiceSoap;
    }

    public CityWeather getWeatherByCityName(String cityName){
        ArrayOfString weatherCity = weatherWebServiceSoap.getWeatherbyCityName(cityName);
        List<String> string = weatherCity.getString();
        StringBuffer cityBuffer=new StringBuffer();
        String province = string.get(0);
        String city = string.get(1);
        if(!"直辖市".equals(province)){
            cityBuffer.append(province);
        }
        if(StringUtils.isBlank(city)){
            return null;
        }else{
            if(!StringUtils.isBlank(cityBuffer.toString())){
                cityBuffer.append("-");
            }
            cityBuffer.append(city);
        }
        String time = string.get(4);
        String temperature = string.get(5);
        String simple = string.get(6);
        String wind = string.get(7);
        String imgOne = string.get(8);
        String imgTwo = string.get(9);
        String infoComplex = string.get(10);
        int index = infoComplex.indexOf("湿度：");
        int indexLast = infoComplex.lastIndexOf("；");
        String humidity = infoComplex.substring(index, indexLast);
        CityWeather cityWeather=new CityWeather(cityBuffer.toString(),temperature,humidity,simple+" "+wind,imgOne,imgTwo,time);
        return cityWeather;
    }
}
