package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.DeviceParamsDoor;
import org.apache.ibatis.annotations.Param;

public interface DeviceParamsDoorDao {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceParamsDoor record);

    int insertSelective(DeviceParamsDoor record);

    DeviceParamsDoor selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceParamsDoor record);

    int updateByPrimaryKey(DeviceParamsDoor record);

    DeviceParamsDoor findByDeviceId(Long deviceId);

    /**
     * 删除设备参数
     * @param deviceId
     * @return
     */
    int deleteByDeviceId(@Param("deviceId") Long deviceId);

    int updateVariableParams(DeviceParamsDoor deviceParamsDoor);
}