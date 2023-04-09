package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.DataTemperaturehumidity;
import org.apache.ibatis.annotations.Param;

public interface DataTemperaturehumidityDao {
    int deleteByPrimaryKey(Long id);

    int insert(DataTemperaturehumidity record);

    int insertSelective(DataTemperaturehumidity record);

    DataTemperaturehumidity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DataTemperaturehumidity record);

    int updateByPrimaryKey(DataTemperaturehumidity record);

    /**
     * 获取最新的温湿度数据
     * @param deviceId
     * @return
     */
    DataTemperaturehumidity getLastedData(@Param("deviceId") Long deviceId);

    /**
     * 删除设备数据
     * @param deviceId
     * @return
     */
    int deleteByDeviceId(@Param("deviceId") Long deviceId);
}