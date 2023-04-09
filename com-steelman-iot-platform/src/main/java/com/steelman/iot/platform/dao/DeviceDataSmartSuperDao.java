package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.DeviceDataSmartElec;
import com.steelman.iot.platform.entity.DeviceDataSmartSuper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeviceDataSmartSuperDao {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceDataSmartSuper record);

    int insertSelective(DeviceDataSmartSuper record);

    DeviceDataSmartSuper selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceDataSmartSuper record);

    int updateByPrimaryKey(DeviceDataSmartSuper record);

    List<DeviceDataSmartSuper> getLastedTenData(@Param("deviceId") Long deviceId);

    int deleteByDeviceId(Long deviceId);

    DeviceDataSmartSuper getLastData(@Param("deviceId") Long deviceId);

    List<DeviceDataSmartElec> getLastedTenDataElec(@Param("deviceId") Long deviceId);
}