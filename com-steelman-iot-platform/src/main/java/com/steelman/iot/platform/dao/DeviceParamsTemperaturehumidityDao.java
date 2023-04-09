package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.DeviceParamsTemperaturehumidity;
import org.apache.ibatis.annotations.Param;

public interface DeviceParamsTemperaturehumidityDao {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceParamsTemperaturehumidity record);

    int insertSelective(DeviceParamsTemperaturehumidity record);

    DeviceParamsTemperaturehumidity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceParamsTemperaturehumidity record);

    int updateByPrimaryKey(DeviceParamsTemperaturehumidity record);

    void updateVariableParams(DeviceParamsTemperaturehumidity deviceParamsTemperaturehumidity);

    DeviceParamsTemperaturehumidity findByDeviceId(@Param("deviceId") Long deviceId);

    /**
     * 删除设备参数
     * @param deviceId
     * @return
     */
    int deleteByDeviceId(@Param("deviceId") Long deviceId);
}