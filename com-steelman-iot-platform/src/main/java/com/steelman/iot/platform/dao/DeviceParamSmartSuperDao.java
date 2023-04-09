package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.DeviceParamSmartSuper;
import org.apache.ibatis.annotations.Param;

public interface DeviceParamSmartSuperDao {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceParamSmartSuper record);

    int insertSelective(DeviceParamSmartSuper record);

    DeviceParamSmartSuper selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceParamSmartSuper record);

    int updateByPrimaryKey(DeviceParamSmartSuper record);

    int deleteByDeviceId(@Param("deviceId") Long deviceId);

    DeviceParamSmartSuper findByDeviceId(@Param("deviceId") Long deviceId);

    int updateVariableParams(DeviceParamSmartSuper deviceParamSmartSuper);
}