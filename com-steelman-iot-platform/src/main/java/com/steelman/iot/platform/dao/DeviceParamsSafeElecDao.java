package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.DeviceParamsSafeElec;
import org.apache.ibatis.annotations.Param;

public interface DeviceParamsSafeElecDao {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceParamsSafeElec record);

    int insertSelective(DeviceParamsSafeElec record);

    DeviceParamsSafeElec selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceParamsSafeElec record);

    int updateByPrimaryKey(DeviceParamsSafeElec record);

    DeviceParamsSafeElec findByDeviceId(Long deviceId);

    /**
     * 更新设备参数
     * @param variableSafeParams
     * @return
     */
    int updateVariableParams(DeviceParamsSafeElec variableSafeParams);

    /**
     * 删除设备的参数
     * @param deviceId
     * @return
     */
    int deleteByDeviceId(@Param("deviceId") Long deviceId);
}