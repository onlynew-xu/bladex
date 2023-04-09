package com.steelman.iot.platform.energyManager.dao;

import com.steelman.iot.platform.energyManager.entity.DeviceHourMeasurement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface DeviceHourMeasurementDao {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceHourMeasurement record);

    int insertSelective(DeviceHourMeasurement record);

    DeviceHourMeasurement selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceHourMeasurement record);

    int updateByPrimaryKey(DeviceHourMeasurement record);

    List<DeviceHourMeasurement> selectByDeviceId(@Param("deviceId") Long deviceId);

    int updateNormal(@Param("updateDeviceHourMeasurement") List<DeviceHourMeasurement> updateDeviceHourMeasurement);
}