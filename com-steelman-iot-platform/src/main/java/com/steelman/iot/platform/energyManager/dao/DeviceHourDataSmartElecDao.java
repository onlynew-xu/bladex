package com.steelman.iot.platform.energyManager.dao;

import com.steelman.iot.platform.energyManager.entity.DeviceHourDataSmartElec;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeviceHourDataSmartElecDao {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceHourDataSmartElec record);

    int insertSelective(DeviceHourDataSmartElec record);

    DeviceHourDataSmartElec selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceHourDataSmartElec record);

    int updateByPrimaryKey(DeviceHourDataSmartElec record);

    List<DeviceHourDataSmartElec> getDayData(@Param("deviceId") Long deviceId, @Param("yearMonthDay") String yearMonthDay);
}