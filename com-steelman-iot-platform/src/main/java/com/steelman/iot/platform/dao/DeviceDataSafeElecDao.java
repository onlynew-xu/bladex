package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.DeviceDataSafeElec;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeviceDataSafeElecDao {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceDataSafeElec record);

    int insertSelective(DeviceDataSafeElec record);

    DeviceDataSafeElec selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceDataSafeElec record);

    int updateByPrimaryKey(DeviceDataSafeElec record);


    List<DeviceDataSafeElec> selectBySbidLimit(Long deviceId);

    int deleteByDeviceId(@Param("deviceId") Long deviceId);
}