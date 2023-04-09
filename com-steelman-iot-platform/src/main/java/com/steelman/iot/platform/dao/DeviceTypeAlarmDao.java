package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.DeviceTypeAlarm;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeviceTypeAlarmDao {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceTypeAlarm record);

    int insertSelective(DeviceTypeAlarm record);

    DeviceTypeAlarm selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceTypeAlarm record);

    int updateByPrimaryKey(DeviceTypeAlarm record);

    List<DeviceTypeAlarm> selectByTypeAlarm(DeviceTypeAlarm record);

    List<DeviceTypeAlarm> selectByDeviceTypeAndSystem(@Param("deviceTypeId") Long deviceTypeId, @Param("systemList") List<Long> systemList);
}