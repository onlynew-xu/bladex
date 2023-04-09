package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.DeviceParamsSmoke;
import org.apache.ibatis.annotations.Param;

public interface DeviceParamsSmokeDao {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceParamsSmoke record);

    int insertSelective(DeviceParamsSmoke record);

    DeviceParamsSmoke selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceParamsSmoke record);

    int updateByPrimaryKey(DeviceParamsSmoke record);

    DeviceParamsSmoke findByDeviceId(Long deviceId);

    int updateVariableParams(DeviceParamsSmoke variableParamsSmoke);

    /**
     * 删除设备参数
     * @param deviceId
     * @return
     */
    int deleteByDeviceId(@Param("deviceId") Long deviceId);
}