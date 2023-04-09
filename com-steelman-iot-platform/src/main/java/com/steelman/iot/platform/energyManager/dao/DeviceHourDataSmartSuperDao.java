package com.steelman.iot.platform.energyManager.dao;

import com.steelman.iot.platform.energyManager.entity.DeviceHourDataSmartSuper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeviceHourDataSmartSuperDao {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceHourDataSmartSuper record);

    int insertSelective(DeviceHourDataSmartSuper record);

    DeviceHourDataSmartSuper selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceHourDataSmartSuper record);

    int updateByPrimaryKey(DeviceHourDataSmartSuper record);

    /**
     * 获取指定日期的时数据
     * @param deviceId
     * @param yearMonthDay
     * @return
     */
    List<DeviceHourDataSmartSuper> getDayData(@Param("deviceId") Long deviceId, @Param("yearMonthDay") String yearMonthDay);
}