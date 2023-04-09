package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.ParamsTemperaturehumidity;

public interface ParamsTemperaturehumidityDao {
    int deleteByPrimaryKey(Long id);

    int insert(ParamsTemperaturehumidity record);

    int insertSelective(ParamsTemperaturehumidity record);

    ParamsTemperaturehumidity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ParamsTemperaturehumidity record);

    int updateByPrimaryKey(ParamsTemperaturehumidity record);
}