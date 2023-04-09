package com.steelman.iot.platform.controller;

import com.google.gson.reflect.TypeToken;
import com.steelman.iot.platform.utils.CommonUtils;
import com.steelman.iot.platform.utils.ExceptionUtils;
import com.steelman.iot.platform.utils.JsonUtils;
import com.steelman.iot.platform.utils.Result;
import com.steelman.iot.platform.weather.config.WeatherUtils;
import com.steelman.iot.platform.weather.config.entity.CityWeather;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.Map;

@RestController
@RequestMapping("/api/weather")
public class WeatherController extends BaseController  {
    @Resource
    private WeatherUtils weatherUtils;

    @PostMapping(value = "/city/name",produces = CommonUtils.MediaTypeJSON)
    public String getWeatherByCityName(@RequestBody Map<String,Object> paramData){
        Result<CityWeather> result=new Result<>();
        result.setCode(0);
        try {
            String city = paramData.get("city").toString();
            if(StringUtils.isEmpty(city)){
                Result.paramError(result);
            }else{
                CityWeather weatherByCityName = weatherUtils.getWeatherByCityName(city);
                if(weatherByCityName==null){
                    result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(weatherByCityName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/weather/city/name"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<CityWeather>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }
}
