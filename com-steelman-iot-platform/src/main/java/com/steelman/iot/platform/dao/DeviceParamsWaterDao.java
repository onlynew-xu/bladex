package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.DeviceParamsWater;
import org.apache.ibatis.annotations.Param;

public interface DeviceParamsWaterDao {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceParamsWater record);

    int insertSelective(DeviceParamsWater record);

    DeviceParamsWater selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceParamsWater record);

    int updateByPrimaryKey(DeviceParamsWater record);

     DeviceParamsWater findByDeviceId(Long deviceId);

    void updateVariableParams(DeviceParamsWater variableParamsWater);

    /**
     * 删除设备参数
     * @param deviceId
     * @return
     */
    int deleteByDeviceId(@Param("deviceId") Long deviceId);
}