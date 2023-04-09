package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.DeviceParamsSmartElec;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeviceParamsSmartElecDao {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceParamsSmartElec record);

    int insertSelective(DeviceParamsSmartElec record);

    DeviceParamsSmartElec selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceParamsSmartElec record);

    int updateByPrimaryKey(DeviceParamsSmartElec record);

    DeviceParamsSmartElec findByDeviceId(Long deviceId);

    List<DeviceParamsSmartElec> selectDeviceByPowerId(Long powerId);

    List<DeviceParamsSmartElec> selectTransformerIncomingDevice(Long transformerId);

    /**
     * 修改设备参数接口
     * @param variableSmartParams
     * @return
     */
    int updateVariableParams(DeviceParamsSmartElec variableSmartParams);

    /**
     * 删除设备参数
     * @param deviceId
     * @return
     */
    int deleteByDeviceId(@Param("deviceId") Long deviceId);
}