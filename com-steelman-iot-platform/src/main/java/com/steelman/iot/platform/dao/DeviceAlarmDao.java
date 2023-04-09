package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.DeviceAlarm;
import org.apache.ibatis.annotations.Param;

public interface DeviceAlarmDao {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceAlarm record);

    int insertSelective(DeviceAlarm record);

    DeviceAlarm selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceAlarm record);

    int updateByPrimaryKey(DeviceAlarm record);

    /**
     * 删除设备的预警信息
     * @param deviceId
     * @return
     */
    int removeByDeviceId(@Param("deviceId") Long deviceId);
}