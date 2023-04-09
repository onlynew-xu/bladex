package com.steelman.iot.platform.energyManager.dao;

import com.steelman.iot.platform.energyManager.entity.DeviceHourDifferMeasurement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface DeviceHourDifferMeasurementDao {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceHourDifferMeasurement record);

    int insertSelective(DeviceHourDifferMeasurement record);

    DeviceHourDifferMeasurement selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceHourDifferMeasurement record);

    int updateByPrimaryKey(DeviceHourDifferMeasurement record);

    List<DeviceHourDifferMeasurement> selectByDeviceId(@Param("deviceId") Long deviceId);

    int updateNormal(@Param("updateDeviceHourDiff") List<DeviceHourDifferMeasurement> updateDeviceHourDiff);
}